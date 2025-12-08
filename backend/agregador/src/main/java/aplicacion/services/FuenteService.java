package aplicacion.services;

import aplicacion.domain.colecciones.fuentes.*;
import aplicacion.dto.input.FuenteAliasDto;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.input.FuenteProxyInputDto;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.mappers.FuenteInputMapper;
import aplicacion.dto.mappers.FuenteProxyInputMapper;
import aplicacion.dto.mappers.HechoInputMapper;
import aplicacion.excepciones.FuenteNoEncontradaException;
import aplicacion.domain.hechos.Hecho;
import aplicacion.excepciones.TipoDeFuenteErroneoException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import aplicacion.repositories.FuenteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
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
        return existente.orElseGet(() -> fuenteRepository.save(fuente));
    }

    @Transactional
    public void guardarFuentes(List<FuenteInputDto> fuentesDto) {
        List<Fuente> fuentes = fuentesDto.stream().map(fuenteInputMapper::map).toList();
        for (Fuente fuente: fuentes) {
            guardarFuente(fuente); // Se guarda las fuentes que no existan en el repositorio, porque podría ocurrir que entre colecciones repitan fuentes
        }
    }

    @Transactional
    public void guardarFuentesSiNoExisten(List< ? extends FuenteInputDto> fuentesDto) {
        List<Fuente> fuentes = fuentesDto.stream().map(fuenteInputMapper::map).toList();
        for (Fuente fuente: fuentes) {
            guardarFuenteSiNoExiste(fuente); // Las fuentes que ya existen no se guardan
        }
    }

    @Transactional
    public Map<Fuente, List<Hecho>> hechosUltimaPeticion(Set<Fuente> fuentes) { // Retornamos una lista de pares, donde el primer elemento es la lista de hechos y el segundo elemento es la fuente de donde se obtuvieron los hechos
        Map<Fuente, List<Hecho>> hashMap = new HashMap<>();

        for (Fuente fuente : fuentes) {
            String uri = this.obtenerUri(fuente);//todo trycatchear
            List<HechoInputDto> hechosDto = getHechosUltimaPeticion(uri, fuente);
            //fuente.getHechosUltimaPeticion(discoveryClient, loadBalancerClient);
            List<Hecho> hechos = hechosDto.stream().map(hechoInputMapper::map).toList();
            guardarFuente(fuente); // Updateo la fuente
            //entityManager.flush(); // En teoria fuerza la actualizacion
            System.out.println("Fuente " + fuente.getId() + " actualizada con última petición: " + fuente.getUltimaPeticion());
            hashMap.put(fuente, hechos);
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
            System.out.println(url);
            response = restTemplate.getForEntity(url, String.class);
            json = response.getBody();
            hechos = mapper.readValue(json, new TypeReference<>() {});
            System.out.println("Se recibieron correctamente los hechos de la fuente " + fuente.getId() + " " + fuente.getAlias());
        } catch (Exception e) {
            fuente.setUltimaPeticion(fechaAnterior); // rollback si falla
            System.err.println("⚠️ Error al consumir la API en fuente " + fuente.getId() + ": " + e.getMessage());
        }

        return hechos;
    }



    private String obtenerUri(Fuente fuente) {
        List<ServiceInstance> proxy = Optional.ofNullable(discoveryClient.getInstances("fuentesProxy")).orElse(Collections.emptyList());
        List<ServiceInstance> dinamicas = Optional.ofNullable(discoveryClient.getInstances("fuentesDinamicas")).orElse(Collections.emptyList());
        List<ServiceInstance> estaticas = Optional.ofNullable(discoveryClient.getInstances("fuentesEstaticas")).orElse(Collections.emptyList());

        System.out.println("proxy count " + proxy.size() + "\n");
        System.out.println("dinamicas count " + dinamicas.size());
        System.out.println("estaticas count " + estaticas.size());

        Stream<ServiceInstance> combinadas = Stream.of(proxy, dinamicas, estaticas).flatMap(Collection::stream);
        Stream<ServiceInstance> h = combinadas;
        System.out.println(h.map(ServiceInstance::getMetadata).map(metadata -> metadata.get("fuentesDisponibles")).toList());
        System.out.println("Buscando URI para la fuente " + fuente.getId());
        ServiceInstance instance = combinadas
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
            throw new RuntimeException("No se pudo encontrar una instancia del servicio 'agregador-fuentes'");
        }
    }

    @Transactional
    public List<Hecho> obtenerHechosPorFuente(String idFuente) {
        return fuenteRepository.findHechosByFuenteId(idFuente);
    }
    public Fuente obtenerFuentePorId(String fuenteId) throws FuenteNoEncontradaException {
        try{
            return fuenteRepository.findById(fuenteId).orElseThrow(()->new FuenteNoEncontradaException("No se encontró la fuente con id: " + fuenteId));
        } catch (FuenteNoEncontradaException e){
            try{
                List<ServiceInstance> proxy = Optional.ofNullable(discoveryClient.getInstances("fuentesProxy")).orElse(Collections.emptyList());
                List<ServiceInstance> dinamicas = Optional.ofNullable(discoveryClient.getInstances("fuentesDinamicas")).orElse(Collections.emptyList());
                List<ServiceInstance> estaticas = Optional.ofNullable(discoveryClient.getInstances("fuentesEstaticas")).orElse(Collections.emptyList());

                Stream<ServiceInstance> combinadas = Stream.of(proxy, dinamicas, estaticas).flatMap(Collection::stream);
                String tipoFuente = combinadas
                        .filter(instance -> {
                            String fuentesDisponibles = instance.getMetadata().get("fuentesDisponibles");
                            if (fuentesDisponibles == null) return false;

                            return Arrays.stream(fuentesDisponibles.split(","))
                                    .map(String::trim)
                                    .anyMatch(id -> id.equals(fuenteId));
                        })
                        .findFirst()
                        .map(instance -> instance.getMetadata().get("tipoFuente"))
                        .orElseThrow(() -> new FuenteNoEncontradaException("Fuente " + fuenteId + " no encontrada en Eureka"));
                switch (tipoFuente) {
                    case "estatica" -> {
                        System.out.println("Creando fuente estatica con id: " + fuenteId);
                        return new FuenteEstatica(fuenteId);
                    }
                    case "dinamica" -> {
                        System.out.println("Creando fuente dinamica con id: " + fuenteId);
                        return new FuenteDinamica(fuenteId);
                    }
                    case "proxy" -> {
                        System.out.println("Creando fuente prxoy con id: " + fuenteId);
                        return new FuenteProxy(fuenteId);
                    }
                    default -> {
                        throw new FuenteNoEncontradaException("Fuente " + fuenteId + " no encontrada y no se pudo determinar su tipo");
                    }
                }
            }catch (FuenteNoEncontradaException ex){

                String agregadorId = discoveryClient.getInstances("AGREGADOR")
                        .stream()
                        .filter(instance -> instance.getMetadata().get("agregadorID").equals(fuenteId))
                        .findFirst()
                        .map(instance -> instance.getMetadata().get("agregadorID"))
                        .orElseThrow(() -> new FuenteNoEncontradaException(fuenteId));
                // ahora tenemos el agregadorId, tenemos que crear la fuente metamapa

                FuenteProxy fuenteProxy;
                try {
                    List<ServiceInstance> proxy = Optional.ofNullable(discoveryClient.getInstances("fuentesProxy")).orElse(Collections.emptyList());
                    List<ServiceInstance> dinamicas = Optional.ofNullable(discoveryClient.getInstances("fuentesDinamicas")).orElse(Collections.emptyList());
                    List<ServiceInstance> estaticas = Optional.ofNullable(discoveryClient.getInstances("fuentesEstaticas")).orElse(Collections.emptyList());

                    Stream<ServiceInstance> combinadas = Stream.of(proxy, dinamicas, estaticas).flatMap(Collection::stream);

                    String fuenteEnProxyId = 
                            combinadas
                            .filter(instance -> {
                                String fuentesDisponibles = instance.getMetadata().get("fuentesDisponibles");
                                return fuentesDisponibles != null && Arrays.asList(fuentesDisponibles.split(",")).contains("agregador-" + agregadorId);
                            })
                            .findFirst()
                            .map(instance -> "agregador-" + agregadorId)
                            .orElseThrow(() -> new FuenteNoEncontradaException("Fuente " + fuenteId + " no encontrada en Eureka como fuente metamapa"));
                    fuenteProxy = new FuenteProxy(fuenteEnProxyId);
                } catch(FuenteNoEncontradaException exe){
                    try {
                        List<ServiceInstance> proxy = Optional.ofNullable(discoveryClient.getInstances("fuentesProxy")).orElse(Collections.emptyList());
                        List<ServiceInstance> dinamicas = Optional.ofNullable(discoveryClient.getInstances("fuentesDinamicas")).orElse(Collections.emptyList());
                        List<ServiceInstance> estaticas = Optional.ofNullable(discoveryClient.getInstances("fuentesEstaticas")).orElse(Collections.emptyList());

                        Stream<ServiceInstance> combinadas = Stream.of(proxy, dinamicas, estaticas).flatMap(Collection::stream);

                        ServiceInstance instancia = 
                                combinadas
                                .filter(instance -> instance.getMetadata().get("tipoFuente").equals("proxy"))
                                .findFirst()
                                .orElseThrow(() -> new FuenteNoEncontradaException("Fuente " + fuenteId + " no encontrada en Eureka como fuente metamapa"));

                        String uri = instancia.getUri().toString() + "/fuentesProxy/fuentesMetamapa";

                        RestTemplate restTemplate = new RestTemplate();

                        FuenteProxyInputDto response = restTemplate.getForObject(uri, FuenteProxyInputDto.class);


                        FuenteProxyInputMapper fuenteProxyInputMapper = new FuenteProxyInputMapper();
                        fuenteProxy = fuenteProxyInputMapper.map(response);
                    } catch (FuenteNoEncontradaException exc) {
                        throw new FuenteNoEncontradaException("Fuente " + fuenteId + " no encontrada en Eureka como fuente metamapa");
                    }
                }
                return this.guardarFuente(fuenteProxy);
            }

        }
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
}