package aplicacion.services;

import aplicacion.domain.colecciones.fuentes.*;
import aplicacion.dto.input.FuenteAliasDto;
import aplicacion.dto.input.FuenteProxyInputDto;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.mappers.FuenteInputMapper;
import aplicacion.dto.mappers.FuenteProxyInputMapper;
import aplicacion.dto.mappers.HechoInputMapper;
import aplicacion.dto.output.AgregadorOutputDto;
import aplicacion.excepciones.FuenteNoEncontradaException;
import aplicacion.domain.hechos.Hecho;
import aplicacion.excepciones.TipoDeFuenteErroneoException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import aplicacion.repositories.FuenteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FuenteService {
    private final FuenteRepository fuenteRepository;
    private final FuenteInputMapper fuenteInputMapper;
    private final HechoInputMapper hechoInputMapper;
    private final DiscoveryClient discoveryClient;
    private final Logger logger = LoggerFactory.getLogger(FuenteService.class);

    //@PersistenceContext
    //private EntityManager entityManager;

    public FuenteService(FuenteRepository fuenteRepository,
                         FuenteInputMapper fuenteInputMapper,
                         HechoInputMapper hechoInputMapper,
                         DiscoveryClient discoveryClient) {
        this.fuenteRepository = fuenteRepository;
        this.fuenteInputMapper = fuenteInputMapper;
        this.hechoInputMapper = hechoInputMapper;
        this.discoveryClient = discoveryClient;
    }

    public Page<Fuente> findByTipo(Integer page, Integer limit, String tipo){
        if(tipo == null)
            return fuenteRepository.findAll(PageRequest.of(page, limit));
        Class<? extends  Fuente> tipoClass = switch (tipo.toLowerCase()) {
            case "estatica", "estática", "e" -> FuenteEstatica.class;
            case "dinamica", "dinámica", "d" -> FuenteDinamica.class;
            case "proxy", "p" -> FuenteProxy.class;
            default -> throw new TipoDeFuenteErroneoException("Tipo de fuente no reconocido: '"+tipo+"'.Se acepta estatica, dinamica y proxy");
        };

        return fuenteRepository.findByTipo(tipoClass, PageRequest.of(page, limit));
    }

    @Transactional
    public Fuente guardarFuente(Fuente fuente) {
        return fuenteRepository.save(fuente);
    }
    /**
     * Recibe una Fuente
     * Busca en la BD si la fuente existe por su ID
     * Si existe, modifica su campo conexion por la conexion de la fuente recibida
     * Si no existe, la crea
     * */


    // Busca si una fuente ya existe segun su id. Si no existe la guarda y la devuelve, si ya existe la devuelve.
    @Transactional
    public Fuente guardarFuenteSiNoExiste(Fuente fuente) {
        Optional<Fuente> existente = fuenteRepository.findById(fuente.getId());
        if(existente.isEmpty()){
            Fuente returnFuente = fuenteRepository.save(fuente);
            logger.info("Fuente creada: {}", fuente.getId());
            return returnFuente;
        }
        return existente.get();
    }

    @Transactional
    public void guardarFuentesSiNoExisten(List< ? extends Fuente> fuentes) {
        for (Fuente fuente: fuentes) {
            guardarFuenteSiNoExiste(fuente);
        }
    }

    @Transactional
    public Map<Fuente, List<Hecho>> hechosUltimaPeticion(Set<Fuente> fuentes) { // Retornamos una lista de pares, donde el primer elemento es la lista de hechos y el segundo elemento es la fuente de donde se obtuvieron los hechos
        Map<Fuente, List<Hecho>> hashMap = new HashMap<>();

        for (Fuente fuente : fuentes) {
            try {
                String uri = this.obtenerUri(fuente);
                List<HechoInputDto> hechosDto = getHechosUltimaPeticion(uri, fuente);
                //fuente.getHechosUltimaPeticion(discoveryClient, loadBalancerClient);
                List<Hecho> hechos = hechosDto.stream().map(hechoInputMapper::map).toList();
                guardarFuente(fuente); // Updateo la fuente
                //entityManager.flush(); // En teoria fuerza la actualizacion
                logger.debug("Fuente {} actualizada con última petición: {}", fuente.getId(), fuente.getUltimaPeticion());
                hashMap.put(fuente, hechos);
            }catch (RuntimeException e){
                logger.debug(e.getMessage());
            }
        }
        return hashMap;
    }

    public Long obtenerCantidadFuentesConColecciones() {
        return fuenteRepository.countWithColecciones();
        }
    private List<HechoInputDto> getHechosUltimaPeticion(String uri, Fuente fuente) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        if(fuente instanceof FuenteEstatica fuenteEstatica){
            if(fuenteEstatica.getFueConsultada())
                return new ArrayList<>();
            fuenteEstatica.setFueConsultada(true);
        }

        LocalDateTime fechaAnterior = fuente.getUltimaPeticion();
        List<HechoInputDto> hechos = new ArrayList<>();

        String url = uri;

        if (fechaAnterior != null) {
            url += "?fechaMayorA=" + fechaAnterior;
        }

        fuente.setUltimaPeticion(LocalDateTime.now());
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response;
            String json;
            response = restTemplate.getForEntity(url, String.class);
            json = response.getBody();
            hechos = mapper.readValue(json, new TypeReference<>() {});
            logger.debug("Se recibieron correctamente los hechos de la fuente {} {}", fuente.getId(), fuente.getAlias());
        } catch (Exception e) {
            fuente.setUltimaPeticion(fechaAnterior); // rollback si falla
            logger.warn("⚠\uFE0F Error al consumir la API en fuente {}: {}", fuente.getId(), e.getMessage());
        }

        return hechos;
    }



    private String obtenerUri(Fuente fuente) {
        List<ServiceInstance> proxy = Optional.ofNullable(discoveryClient.getInstances("fuentesProxy")).orElse(Collections.emptyList());
        List<ServiceInstance> dinamicas = Optional.ofNullable(discoveryClient.getInstances("fuentesDinamicas")).orElse(Collections.emptyList());
        List<ServiceInstance> estaticas = Optional.ofNullable(discoveryClient.getInstances("fuentesEstaticas")).orElse(Collections.emptyList());

        logger.debug("Instancias de fuentes online  -->  proxy: {}, dinámicas: {}, estáticas: {}", proxy.size(), dinamicas.size(), estaticas.size());

        List<ServiceInstance> combinadas = Stream.of(proxy, dinamicas, estaticas).flatMap(Collection::stream).toList();
        logger.debug("Buscando URI para: {} | Fuentes disponibles: {}", fuente.getId(), combinadas.stream()
                .map(inst -> inst.getMetadata().get("fuentesDisponibles"))
                .toList());
        ServiceInstance instance = combinadas.stream()
                .filter(inst -> inst.getMetadata().get("fuentesDisponibles").contains(fuente.getId()))//TODO cambiar metadata en fuenteDinamica y fuenteProxy
                .findFirst()
                .orElse(null);

        if (instance != null) {
            String uri = instance.getUri().toString();
            switch (fuente.getClass().getSimpleName()) {
                case "FuenteEstatica" -> uri = uri + "/fuentesEstaticas/" + fuente.getId() + "/hechos";
                case "FuenteDinamica" -> uri = uri + "/fuentesDinamicas/hechos";
                case "FuenteProxy" -> uri = uri + "/fuentesProxy/"+ fuente.getId() + "/hechos";
            }
            return uri;
        } else {
            throw new RuntimeException("No se pudo encontrar una instancia que contenga la fuente " + fuente.getAlias() + "  -- " + fuente.getId() );
        }
    }

    @Transactional
    public List<Hecho> obtenerHechosPorFuente(String idFuente) {
        return fuenteRepository.findHechosByFuenteId(idFuente);
    }
    public Fuente obtenerFuentePorId(String fuenteId) throws FuenteNoEncontradaException {

        return fuenteRepository.findById(fuenteId).orElseThrow(()->new FuenteNoEncontradaException("No se encontró la fuente con id: " + fuenteId));
        
    }

    public List<Fuente> obtenerTodasLasFuentes(){
        return fuenteRepository.findAll();
    }
    @Transactional
    public Fuente cambiarAlias(String id, FuenteAliasDto fuenteAliasDto) {
        Fuente fuente = obtenerFuentePorId(id);
        fuente.setAlias(fuenteAliasDto.getAlias());
        fuenteRepository.save(fuente);
        return fuente;
    }

    public Set<String> obtenerFuentesDisponiblesEnEureka( ) {//todo agregar informacion necesaria para elegir las fuentes en metadatos
        List<String> fuentesIds;
        List<ServiceInstance> proxy = Optional.ofNullable(discoveryClient.getInstances("fuentesProxy")).orElse(Collections.emptyList());
        List<ServiceInstance> dinamicas = Optional.ofNullable(discoveryClient.getInstances("fuentesDinamicas")).orElse(Collections.emptyList());
        List<ServiceInstance> estaticas = Optional.ofNullable(discoveryClient.getInstances("fuentesEstaticas")).orElse(Collections.emptyList());

        Stream<ServiceInstance> combinadas = Stream.of(proxy, dinamicas, estaticas).flatMap(Collection::stream);

        fuentesIds =
                combinadas
                .map(instance -> instance.getMetadata().get("fuentesDisponibles"))
                .toList();
        List <String> agregadores;
        agregadores = discoveryClient.getInstances("AGREGADOR")
                .stream()
                .map(instance -> instance.getMetadata().get("agregadorID"))
                .map(agregadorid -> "agregador-"+agregadorid)
                .toList();

        Set<String> setFuentesIds = new HashSet<>(fuentesIds);
        setFuentesIds.addAll(agregadores);
        setFuentesIds.addAll(this.obtenerTodasLasFuentes().stream().map(Fuente::getId).collect(Collectors.toSet()));
        return setFuentesIds;
    }

    public List<AgregadorOutputDto> getAgregadores() {
        List <String> agregadores = discoveryClient.getInstances("agregador")
                .stream()
                .map(instance -> instance.getMetadata().get("agregadorID"))
                .map(agregadorid -> "agregador-"+agregadorid)
                .toList();

        return agregadores.stream().map(AgregadorOutputDto::new).toList();
    }
}