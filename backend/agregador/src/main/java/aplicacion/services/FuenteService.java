package aplicacion.services;

import aplicacion.domain.colecciones.fuentes.*;
import aplicacion.dto.input.FuenteAliasDto;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.mappers.FuenteInputMapper;
import aplicacion.dto.mappers.HechoInputMapper;
import aplicacion.excepciones.FuenteNoEncontradaException;
import aplicacion.domain.hechos.Hecho;
import aplicacion.excepciones.TipoDeFuenteErroneoException;
import aplicacion.repositorios.RepositorioDeFuentes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;

@Service
public class FuenteService {
    private final RepositorioDeFuentes repositorioDeFuentes;
    private final FuenteInputMapper fuenteInputMapper;
    private final HechoInputMapper hechoInputMapper;
    private final DiscoveryClient discoveryClient;
    private final LoadBalancerClient loadBalancerClient;
    //@PersistenceContext
    //private EntityManager entityManager;

    public FuenteService(RepositorioDeFuentes repositorioDeFuentes,
                         FuenteInputMapper fuenteInputMapper,
                         HechoInputMapper hechoInputMapper,
                         DiscoveryClient discoveryClient,
                         LoadBalancerClient loadBalancerClient) {
        this.repositorioDeFuentes = repositorioDeFuentes;
        this.fuenteInputMapper = fuenteInputMapper;
        this.hechoInputMapper = hechoInputMapper;
        this.discoveryClient = discoveryClient;
        this.loadBalancerClient = loadBalancerClient;
    }

    public Page<Fuente> findByTipo(Integer page, Integer limit, String tipo){
        if(tipo == null)
            return repositorioDeFuentes.findAll(PageRequest.of(page, limit));
        Class<? extends  Fuente> tipoClass = switch (tipo.toLowerCase()) {
            case "estatica", "estática", "e" -> FuenteEstatica.class;
            case "dinamica", "dinámica", "d" -> FuenteDinamica.class;
            case "proxy", "p" -> FuenteProxy.class;
            default -> throw new TipoDeFuenteErroneoException("Tipo de fuente no reconocido: '"+tipo+"'.Se acepta estatica, dinamica y proxy");
        };

        return repositorioDeFuentes.findByTipo(tipoClass, PageRequest.of(page, limit));
    }

    @Transactional
    public Fuente guardarFuente(Fuente fuente) {
        return repositorioDeFuentes.save(fuente);
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
        Optional<Fuente> existente = repositorioDeFuentes.findById(fuente.getId());
        return existente.orElseGet(() -> repositorioDeFuentes.save(fuente));
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
    public Map<Fuente, List<Hecho>> hechosUltimaPeticion(Set<Fuente> fuentes, Map<Fuente, ServiceInstance> fuenteProxyInstanceMap) { // Retornamos una lista de pares, donde el primer elemento es la lista de hechos y el segundo elemento es la fuente de donde se obtuvieron los hechos
        Map<Fuente, List<Hecho>> hashMap = new HashMap<>();
        System.out.println("cantidad de fuentes" + fuentes.size());

        for (Fuente fuente : fuentes) {
            //List<Hecho> hechos = new ArrayList<>(); // Lista de hechos que se van a retornar
            System.out.println("capo en FuenteService");

            if (fuente instanceof FuenteProxy) {
                ServiceInstance instance = fuenteProxyInstanceMap.get(fuente);
                System.out.println("ACA DEBERIA EXPLOTAR SI NO SE EJECUTA"+fuente);
                List<HechoInputDto> hechosDto = fuente.getHechosUltimaPeticion(discoveryClient, loadBalancerClient, instance);
                System.out.println(hechosDto);
                List<Hecho> hechos = hechosDto.stream().map(hechoInputMapper::map).toList();
                guardarFuente(fuente);
                hashMap.put(fuente, hechos);
                continue;
            }
            System.out.println("ACA DEBERIA EXPLOTAR SI NO SE EJECUTA"+fuente);
            List<HechoInputDto> hechosDto = fuente.getHechosUltimaPeticion(discoveryClient, loadBalancerClient, null);
            System.out.println(hechosDto);
            List<Hecho> hechos = hechosDto.stream().map(hechoInputMapper::map).toList();
            guardarFuente(fuente); // Updateo la fuente
            //entityManager.flush(); // En teoria fuerza la actualizacion
            System.out.println("Fuente " + fuente.getId() + " actualizada con última petición: " + fuente.getUltimaPeticion());
            hashMap.put(fuente, hechos);
        }
        return hashMap;
    }

    public Long obtenerCantidadFuentes() {
        return repositorioDeFuentes.count();
    }


    @Transactional
    public List<Hecho> obtenerHechosPorFuente(String idFuente) {
        return repositorioDeFuentes.findHechosByFuenteId(idFuente);
    }

    public Fuente obtenerFuentePorId(String fuenteId) throws FuenteNoEncontradaException {
        return repositorioDeFuentes.findById(fuenteId).orElseThrow(()->new FuenteNoEncontradaException("No se encontró la fuente con id: " + fuenteId));
    }

    public List<Fuente> obtenerTodasLasFuentes(){
        return repositorioDeFuentes.findAll();
    }
    @Transactional
    public Fuente cambiarAlias(String id, FuenteAliasDto fuenteAliasDto) {
        Fuente fuente = obtenerFuentePorId(id);
        fuente.setAlias(fuenteAliasDto.getAlias());
        repositorioDeFuentes.save(fuente);
        return fuente;
    }
}