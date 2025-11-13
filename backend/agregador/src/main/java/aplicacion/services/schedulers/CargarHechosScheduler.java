package aplicacion.services.schedulers;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.clasesIntermedias.HechoXColeccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.FuenteDinamica;
import aplicacion.domain.colecciones.fuentes.FuenteEstatica;
import aplicacion.domain.colecciones.fuentes.FuenteProxy;
import aplicacion.domain.hechos.Hecho;
import aplicacion.excepciones.FuenteNoEncontradaException;
import aplicacion.services.ColeccionService;
import aplicacion.services.FuenteService;
import aplicacion.services.HechoService;
import aplicacion.services.depurador.DepuradorDeHechos;
import aplicacion.services.normalizador.NormalizadorDeHechos;
import aplicacion.utils.ProgressBar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;


import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class CargarHechosScheduler {
    private final FuenteService fuenteService;
    private final ColeccionService coleccionService;
    private final NormalizadorDeHechos normalizadorDeHechos;
    private final DepuradorDeHechos depuradorDeHechos;
    private final HechoService hechoService;
    private final DiscoveryClient discoveryClient;
    @Value("${hechos.lazy-loading}")
    boolean hechosSeCarganSoloSiEstanEnUnaColeccion;
    public CargarHechosScheduler(FuenteService fuenteService, ColeccionService coleccionService, NormalizadorDeHechos normalizadorDeHechos, DepuradorDeHechos depuradorDeHechos, HechoService hechoService, DiscoveryClient discoveryClient) {
        this.fuenteService = fuenteService;
        this.coleccionService = coleccionService;
        this.normalizadorDeHechos = normalizadorDeHechos;
        this.depuradorDeHechos = depuradorDeHechos;
        this.hechoService = hechoService;
        this.discoveryClient = discoveryClient;
    }

    @Scheduled(initialDelay = 10000L,fixedRate = 3600000) // Se ejecuta cada 1 hora
    @Transactional
    public void cargarHechos() {
        System.out.println("Se ha iniciado la carga de hechos de las fuentes remotas. Esto puede tardar un rato. ("+ LocalDateTime.now() + ")");
        // Pedirle a eureka las instancias
        List<ServiceInstance> proxyInstances = discoveryClient.getInstances("fuenteProxy");
        List<ServiceInstance> estaticaInstances = discoveryClient.getInstances("fuenteEstatica");
        List<ServiceInstance> dinamicaInstances = discoveryClient.getInstances("fuenteDinamica");
        List<ServiceInstance> services = new ArrayList<>();
        services.addAll(proxyInstances);
        services.addAll(estaticaInstances);
        services.addAll(dinamicaInstances);
        // A cada instancia le pedimos las fuentes
        // URI = http://ipServicio:puertoServicio/(tipoDeFuente)
        List<String> proxyURIs = proxyInstances.stream().map(ServiceInstance::getUri).map(s->s.toString() + "/fuentesProxy").toList();
        /*List<String> estaticaURIS = estaticaInstances.stream().map(ServiceInstance::getUri).map(s->s.toString() + "/fuentesEstaticas").toList();
        List<String> dinamicaURIS = dinamicaInstances.stream().map(ServiceInstance::getUri).map(s->s.toString() + "/fuentesDinamicas").toList();
        List<String> allUris = new ArrayList<>();
        allUris.addAll(proxyURIs);
        allUris.addAll(estaticaURIS);
        allUris.addAll(dinamicaURIS);

        // Asociamos temporalmente las fuentes a las instancias halladas
        Map<Fuente, String> URIxFuente = new HashMap<>();
        System.out.println("Instancias: " + allUris.size());
        if(allUris.isEmpty()){
            return;
        }
        System.out.println(allUris.getFirst().toString());*/
        //Map<Fuente, String> URIxFuente = new HashMap<>();

        Map<Fuente, ServiceInstance> fuenteProxyInstanceMap = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        for(ServiceInstance instance : services) {
            String serviceName = switch (instance.getServiceId()) {
                case "FUENTEPROXY" -> "/fuentesProxy";
                case "FUENTEESTATICA" -> "/fuentesEstaticas";
                case "FUENTEDINAMICA" -> "/fuentesDinamicas";
                default -> throw new RuntimeException("ServiceId desconocido: " + instance.getServiceId());
            };

            String uri = instance.getUri().toString() + serviceName;

            List<String> fuentesIds = List.of(Objects.requireNonNull(restTemplate.getForEntity(uri, String[].class).getBody()));
            System.out.println(fuentesIds);
            fuentesIds.forEach(fuenteId->{
                        Fuente fuente;
                        try{
                            fuente = fuenteService.obtenerFuentePorId(fuenteId);
                        } catch (FuenteNoEncontradaException e) {
                            if(uri.endsWith("Proxy")){
                                fuente = new FuenteProxy(fuenteId, instance.getServiceId());

                            }
                            else if (uri.endsWith("Estaticas")){
                                fuente = new FuenteEstatica(fuenteId, instance.getServiceId());
                            }
                            else if (uri.endsWith("Dinamicas")){
                                fuente = new FuenteDinamica(fuenteId, instance.getServiceId());
                            }
                            else{
                                throw new RuntimeException("Anduvo mal lo de reconcocer la fuente");
                            }
                            // Persistimos las fuentes que no esten guardadas
                            fuenteService.guardarFuente(fuente);

                        }
                        fuenteProxyInstanceMap.put(fuente, instance);

                //URIxFuente.put(fuente, uri + "/" + fuenteId);
                    }
            );

        }
        fuenteProxyInstanceMap.entrySet().removeIf(e -> !(e.getKey() instanceof FuenteProxy));

        // Buscamos las fuentes que estamos usando en colecciones

        List<Coleccion> colecciones = coleccionService.obtenerColecciones();
        Set<Fuente> fuenteSet = new HashSet<>();
        if(hechosSeCarganSoloSiEstanEnUnaColeccion){
            System.out.println("esta en coleccion");
            for (Coleccion coleccion : colecciones){
                fuenteSet.addAll(coleccion.getFuentes());
            }
        }else{
            System.out.println("no estsa en coleccion");
            fuenteSet.addAll(fuenteService.obtenerTodasLasFuentes());
        }

        //Map<Fuente, String> fuentesRelevantesMap = new HashMap<>();

        /*for(Fuente fuenteRelevante : fuenteSet){
            fuentesRelevantesMap.put(fuenteRelevante, URIxFuente.get(fuenteRelevante));
        }*/
        System.out.println("Se pediran hechos de " + fuenteSet.size() + " fuentes");


        // Pedimos los hechos a cada fuente
        Map<Fuente, List<Hecho>> hechosPorFuente = fuenteService.hechosUltimaPeticion(fuenteSet, fuenteProxyInstanceMap);
        System.out.println("luego de pedir hechos");


        normalizadorDeHechos.normalizarTodos(hechosPorFuente);
        depuradorDeHechos.depurar(hechosPorFuente); // Depura hechos repetidos

        System.out.println("""
        
        
        ============================
         Carga de hechos finalizada
        ============================
        
        
        Se asignaran los hechos a las colecciones...
        
        """);
        Long inicioAsignacion = System.nanoTime();
        int indiceColeccion = 0;
        int indiceFuente = 0;
        for(Coleccion coleccion : colecciones){
            indiceColeccion++;
            System.out.println("Coleccion: " + indiceColeccion + " / " + colecciones.size());

            for(Fuente fuente : coleccion.getFuentes()){
                indiceFuente++;
                List<Hecho> hechosObtenidos = hechosPorFuente.get(fuente);
                ProgressBar progressBar = new ProgressBar(hechosObtenidos.size(), "Fuente: "+indiceFuente+" / " + coleccion.getFuentes().size());
                // hechosObtenidos = hechosObtenidos.stream.filter(hecho->hecho.noEstaPresente).toList();
                for (Hecho hecho : hechosObtenidos) {
                    HechoXColeccion hechoPorColeccion = new HechoXColeccion(hecho, coleccion);
                    hechoService.guardarHechoPorColeccion(hechoPorColeccion);
                    progressBar.avanzar();
                }
            }
            indiceFuente = 0;
        }
        Long finAsignacion = System.nanoTime();
        System.out.printf("\nSe asignaron los hechos a las colecciones ( %2d ms )\n", (finAsignacion - inicioAsignacion)/1_000_000);

        // abrir map de fuente y lista de hechos, por cada fuente (fuente1, fuente2, ...) cargamos los hechos
        // for (fuente) {fuente.hechos.cargarHechos()} en el metodo que haga esa carga de hechos se hace la validacion de si el hecho ya existe en bd (mediante equals)
        // si ya existe no se carga el hecho pero se carga una entrada en HechoXFuente que asocie esta fuente y el hecho que ya existia
        // si no existe se carga el hecho y se carga la entrada en HechoXFuente
        // si se duplica un hecho dentro de la misma fuente -> Se sobreescribe? Presuponemos que no hay hechos duplicados dentro de una misma fuente
        // en ambos casos se carga la entrada en hechoxfuente, lo que varia es a que hecho apunta.
        // DECISION DE DISEÑO: si un hecho esta duplicado, conservamos el que estaba antes en la base de datos y descartamos el nuevo.
        System.out.println("""
                
                =================================
                 Asignación de hechos finalizada
                =================================
                
                """);
    }

}