package aplicacion.services.schedulers;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.FuenteDinamica;
import aplicacion.domain.colecciones.fuentes.FuenteEstatica;
import aplicacion.domain.colecciones.fuentes.FuenteProxy;
import aplicacion.domain.conexiones.Conexion;
import aplicacion.services.FuenteService;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Stream;

@Component
public class CargarFuentesScheduler {
    private final DiscoveryClient discoveryClient;
    RestTemplate restTemplate = new RestTemplate();
    private final FuenteService fuenteService;
    public CargarFuentesScheduler(DiscoveryClient discoveryClient, FuenteService fuenteService) {
        this.discoveryClient = discoveryClient;
        this.fuenteService = fuenteService;
    }

    @Scheduled(initialDelay = 0, fixedRate = 300000) // Se ejecuta cada 5 minutos
    @Transactional
    public void cargarNuevasFuentes(){
        cargarFuentes();
    }

    public Map<Class<? extends Fuente>, List<ServiceInstance>> obtenerInstanciasDeConexiones(){
        List<ServiceInstance> proxy = Optional.ofNullable(discoveryClient.getInstances("fuentesProxy")).orElse(Collections.emptyList());
        List<ServiceInstance> dinamicas = Optional.ofNullable(discoveryClient.getInstances("fuentesDinamicas")).orElse(Collections.emptyList());
        List<ServiceInstance> estaticas = Optional.ofNullable(discoveryClient.getInstances("fuentesEstaticas")).orElse(Collections.emptyList());

        System.out.println("Fuentes online: proxy-" + proxy.size() + "  dinamica-"+dinamicas.size() + "  estatica-" + estaticas.size());
        return Map.of(FuenteProxy.class, proxy, FuenteEstatica.class, estaticas, FuenteDinamica.class, dinamicas);
    }

    public List<? extends Fuente> obtenerInstanciasDeFuentes(Class<? extends Fuente> tipoFuente, ServiceInstance conexion){
        System.out.println("Descubriendo fuentes de " + conexion.getUri() + "  " + tipoFuente.toString());
        List<? extends Fuente> fuentesObtenidas = null;
        try {
            if (tipoFuente == FuenteEstatica.class) {
                List<String> fuentes = List.of(Objects.requireNonNull(restTemplate.getForEntity(conexion.getUri() + "/fuentesEstaticas", String[].class).getBody()));
                fuentes.forEach(f -> System.out.println("ID: " + f));
                fuentesObtenidas = fuentes.stream().map(FuenteEstatica::new).toList();
            } else if (tipoFuente == FuenteDinamica.class) {
                List<String> fuentes = List.of(Objects.requireNonNull(restTemplate.getForEntity(conexion.getUri() + "/fuentesDinamicas", String[].class).getBody()));
                fuentes.forEach(f -> System.out.println("ID: " + f));
                fuentesObtenidas = fuentes.stream().map(FuenteDinamica::new).toList();
            } else if (tipoFuente == FuenteProxy.class) {
                List<String> fuentes = List.of(Objects.requireNonNull(restTemplate.getForEntity(conexion.getUri() + "/fuentesProxy", String[].class).getBody()));
                fuentes.forEach(f -> System.out.println("ID: " + f));
                fuentesObtenidas = fuentes.stream().map(FuenteProxy::new).toList();
            }
        } catch (Exception e) {
            System.err.println("Error al descubrir nuevas fuentes: " + e.getMessage());
        }
        return fuentesObtenidas;
    }

    private void cargarFuentes(){
        // Esta funcion lo que hace es cargar las nuevas fuentes y dejarlas vacias en la BD
        System.out.println("\nCargando fuentes nuevas:\n");
        Map<Class<? extends Fuente>, List<ServiceInstance>> conexiones = obtenerInstanciasDeConexiones();
        System.out.println("Conexiones obtenidas. Iniciando importacion de fuentes nuevas");
        for (Class<? extends Fuente> tipoFuente : conexiones.keySet()){
            for(ServiceInstance conexion : conexiones.get(tipoFuente)){
                List<? extends Fuente> fuentes = obtenerInstanciasDeFuentes(tipoFuente, conexion);
                if(fuentes == null || fuentes.isEmpty())
                    continue;
                fuenteService.guardarFuentesSiNoExisten(fuentes);
            }
        }
    }
}
