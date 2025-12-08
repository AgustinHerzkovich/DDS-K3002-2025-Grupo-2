package aplicacion.services;

import aplicacion.domain.fuentesProxy.FuenteProxy;
import aplicacion.domain.fuentesProxy.fuentesDemo.FuenteDemo;
import aplicacion.domain.fuentesProxy.fuentesDemo.HechoBuilder;
import aplicacion.domain.fuentesProxy.fuentesMetamapa.FuenteMetamapa;
import aplicacion.dto.input.FuenteProxyInputDto;
import aplicacion.dto.mappers.FuenteProxyInputMapper;
import aplicacion.dto.mappers.FuenteProxyOutputMapper;
import aplicacion.dto.mappers.HechoOutputMapper;
import aplicacion.dto.output.FuenteProxyOutputDto;
import aplicacion.dto.output.HechoOutputDto;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import aplicacion.repositories.FuenteProxyRepository;
import aplicacion.excepciones.FuenteNoEncontradaException;
import org.springframework.stereotype.Service;
import aplicacion.domain.hechos.Hecho;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FuenteProxyService {
    private final FuenteProxyRepository fuenteProxyRepository;
    private final FuenteProxyInputMapper fuenteProxyInputMapper;
    private final FuenteProxyOutputMapper fuenteProxyOutputMapper;
    private final HechoOutputMapper hechoOutputMapper;
    private final DiscoveryClient discoveryClient;


    public FuenteProxyService(DiscoveryClient discoveryClient, FuenteProxyRepository fuenteProxyRepository, FuenteProxyInputMapper fuenteProxyInputMapper, FuenteProxyOutputMapper fuenteProxyOutputMapper, HechoOutputMapper hechoOutputMapper) {
        this.fuenteProxyRepository = fuenteProxyRepository;
        this.fuenteProxyInputMapper = fuenteProxyInputMapper;
        this.fuenteProxyOutputMapper = fuenteProxyOutputMapper;
        this.hechoOutputMapper = hechoOutputMapper;
        this.discoveryClient = discoveryClient;
    }

    public void pedirHechos() {
        List<FuenteProxy> fuentesProxy = fuenteProxyRepository.findAll();
        for (FuenteProxy fuente : fuentesProxy) {
            this.pedirHechosFuente(fuente);
            fuenteProxyRepository.save(fuente);
        }
    }

    public List<HechoOutputDto> importarHechos() {
        List<FuenteProxy> fuentesProxy = fuenteProxyRepository.findAll();
        List<Hecho> listaDeHechosADevolver = new ArrayList<>();
        for (FuenteProxy fuente : fuentesProxy) {
            listaDeHechosADevolver.addAll(this.importarHechosFuentes(fuente));
        }
       return listaDeHechosADevolver.stream().map(hechoOutputMapper::map).toList();
    }

    public List<HechoOutputDto> importarHechosConFechaMayorA(LocalDateTime fechaMayorA) {
        List<FuenteProxy> fuentesProxy = fuenteProxyRepository.findAll();
        List<Hecho> listaDeHechosADevolver = new ArrayList<>();

        for (FuenteProxy fuente : fuentesProxy) {
            listaDeHechosADevolver.addAll(this.importarHechosFuentes(fuente));
        }
        return listaDeHechosADevolver.stream().filter(hecho -> hecho.getFechaUltimaModificacion().isAfter(fechaMayorA)).map(hechoOutputMapper::map).toList();
    }
    @Transactional
    public void pedirHechosFuente (FuenteProxy fuente){ // es fuente demo porque solo lo usa fuentes demo
        if (fuente instanceof FuenteDemo) {
            FuenteDemo fuenteDemo = (FuenteDemo) fuente;
            Map<String, Object> datos;
            Hecho hecho;
            while((datos = fuenteDemo.getBiblioteca().siguienteHecho(fuenteDemo.getUrl(), fuenteDemo.getUltimaConsulta())) != null) {
                hecho = fuenteDemo.getHechoBuilder().construirHecho(datos);
                fuenteDemo.getHechos().add(hecho);
            }
            fuenteDemo.setUltimaConsulta(LocalDateTime.now());

        }

    }
    @Transactional
    public List<Hecho> importarHechosFuentes(FuenteProxy fuente) {
        if (fuente instanceof FuenteMetamapa) {
            RestTemplate restTemplate = new RestTemplate();
            //String url = "https://mocki.io/v1/66ea9586-9ada-4bab-a974-58abbe005292";
            try {
                String endpointHechos = endpointHechosAgregador(((FuenteMetamapa) fuente).getAgregadorID());
                System.out.println("\n\n" + endpointHechos + "\n\n");
                List<Hecho> hechos = List.of(restTemplate.getForObject(endpointHechos, Hecho[].class));
                System.out.println(hechos.isEmpty());
                return hechos;
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("Error al obtener hechos desde el agregador con ID: " + ((FuenteMetamapa) fuente).getAgregadorID());
                return List.of();
            }

        }else {
            return ((FuenteDemo) fuente).getHechos();

        }

    }
    public String endpointHechosAgregador(String agregadorID) {
        String baseURL = discoveryClient.getInstances("AGREGADOR")
                .stream()
                .filter(instance -> instance.getMetadata().get("agregadorID").equals(agregadorID))
                .findFirst()
                .map(instance -> instance.getUri().toString())
                .orElseThrow(() -> new RuntimeException(" No se encontraron instancias para el servicio: " + agregadorID));
        return baseURL + "/agregador/hechosSinPaginar";
    }
    @Transactional
    public List<HechoOutputDto> importarHechosDeFuente(String id) throws FuenteNoEncontradaException {
        FuenteProxy fuente = fuenteProxyRepository.findById(id).orElseThrow(() -> new FuenteNoEncontradaException("Fuente " + id + "no encontrada"));
        return this.importarHechosFuentes(fuente).stream().map(hechoOutputMapper::map).toList();
    }

    public FuenteProxyOutputDto guardarFuente(FuenteProxyInputDto fuenteProxyInputDto) {
        FuenteProxy fuenteProxy = fuenteProxyRepository.save(fuenteProxyInputMapper.map(fuenteProxyInputDto));
        return fuenteProxyOutputMapper.map(fuenteProxy);
    }

    public List<String> obtenerFuentesDisponibles() {
        return fuenteProxyRepository.findAll().stream().map(fp -> fp.getId()).toList();
    }

    public FuenteMetamapa guardarFuenteMetamapa(String agregadorID) {
        FuenteMetamapa fuenteMetamapa = new FuenteMetamapa(agregadorID);
        return fuenteProxyRepository.save(fuenteMetamapa);
    }
}

/* @Override
    public List<Hecho> importarHechos(DiscoveryClient discoveryClient) {


        RestTemplate restTemplate = new RestTemplate();
        //String url = "https://mocki.io/v1/66ea9586-9ada-4bab-a974-58abbe005292";
        try {
            String endpointHechos = endpointHechos(discoveryClient);
            System.out.println("\n\n" + endpointHechos + "\n\n");
            List<Hecho> hechos = List.of(restTemplate.getForObject(endpointHechos, Hecho[].class));
            System.out.println(hechos.isEmpty());
            return hechos;
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Error al obtener hechos desde el agregador con ID: " + agregadorID);
            return List.of();
        }
    }*/

    /*public String endpointHechos(DiscoveryClient discoveryClient) {
        String baseURL = discoveryClient.getInstances("AGREGADOR")
                .stream()
                .filter(instance -> instance.getMetadata().get("agregadorID").equals(agregadorID))
                .findFirst()
                .map(instance -> instance.getUri().toString())
                .orElseThrow(() -> new RuntimeException(" No se encontraron instancias para el servicio: " + agregadorID));
        return baseURL + "/agregador/hechosSinPaginar";
    }*/

//public FuenteDemo() {
    //this.hechoBuilder = new HechoBuilder();
//}
    /*
    @Override
    public void pedirHechos() {
        // basicamente pide hechos hasta que el map que llega esta vacio. Es la logica de negocio que indica el enunciado y es lo que hay que seguir

        // delegar peticion de hechos a la biblioteca
        Map<String, Object> datos;
        Hecho hecho;
        while((datos = biblioteca.siguienteHecho(url, ultimaConsulta)) != null) {
            hecho = hechoBuilder.construirHecho(datos);
            hechos.add(hecho);
        }
    }

    @Override
    public List<Hecho> importarHechos(DiscoveryClient discoveryClient) {
        return hechos;
    }*/