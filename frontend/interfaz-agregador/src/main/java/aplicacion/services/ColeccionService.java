package aplicacion.services;

import aplicacion.dto.GraphQLHechosCuradosResponse;
import aplicacion.dto.GraphQLHechosIrrestrictosResponse;
import aplicacion.dto.PageWrapper;
import aplicacion.dto.output.ColeccionOutputDto;
import aplicacion.dto.output.HechoMapaOutputDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class ColeccionService {
    private WebClient webClient;

    @Value("${api.publica.port}")
    private Integer apiPublicaPort;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:" + apiPublicaPort + "/apiPublica")
                // aumento el buffer para respuestas grandes
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer ->
                                configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024) // 16MB
                        )
                        .build())
                .build();
    }

    public Mono<PageWrapper<ColeccionOutputDto>> obtenerColecciones(int page, int size, String search) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/colecciones");
                    if (search != null && !search.isEmpty()) {
                        uriBuilder.queryParam("search", search);
                    }
                    uriBuilder.queryParam("page", page);
                    uriBuilder.queryParam("size", size);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageWrapper<ColeccionOutputDto>>() {})
                .doOnError(e -> System.err.println("Error al obtener colecciones de la API Pública: " + e.getMessage()));
    }

    public Mono<PageWrapper<HechoMapaOutputDto>> obtenerHechosIrrestrictosDeColeccion(String idColeccion,
                                                                                             String categoria,
                                                                                             String fechaReporteDesde,
                                                                                             String fechaReporteHasta,
                                                                                             String fechaAcontecimientoDesde,
                                                                                             String fechaAcontecimientoHasta,
                                                                                             Double latitud,
                                                                                             Double longitud,
                                                                                             Double radio,
                                                                                             String search,
                                                                                             Integer page,
                                                                                             Integer size) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/colecciones/{idColeccion}/hechosIrrestrictos");
                    return getUri(idColeccion, categoria, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud, radio, search, page, size, uriBuilder);
                })
                .retrieve()
                .bodyToMono(GraphQLHechosIrrestrictosResponse.class)
                .map(graphqlResponse -> {
                    if (graphqlResponse.getData() != null &&
                        graphqlResponse.getData().getHechosPorColeccion() != null) {

                        GraphQLHechosIrrestrictosResponse.HechosWrapper wrapper =
                            graphqlResponse.getData().getHechosPorColeccion();
                        GraphQLHechosIrrestrictosResponse.PageInfo pageInfo = wrapper.getPageInfo();

                        PageWrapper<HechoMapaOutputDto> pageWrapper = new PageWrapper<>();
                        pageWrapper.setContent(wrapper.getContent());
                        pageWrapper.setNumber(pageInfo.getNumber());
                        pageWrapper.setSize(pageInfo.getSize());
                        pageWrapper.setTotalElements(pageInfo.getTotalElements());
                        pageWrapper.setTotalPages(pageInfo.getTotalPages());
                        pageWrapper.setFirst(pageInfo.getNumber() == 0);
                        pageWrapper.setLast(!pageInfo.isHasNext());

                        return pageWrapper;
                    }
                    PageWrapper<HechoMapaOutputDto> emptyWrapper = new PageWrapper<>();
                    emptyWrapper.setContent(new java.util.ArrayList<>());
                    return emptyWrapper;
                })
                .doOnError(e -> System.err.println("Error al obtener hechos irrestrictos de la colección de la API Pública: " + e.getMessage()));
    }

    public Mono<PageWrapper<HechoMapaOutputDto>> obtenerHechosCuradosDeColeccion(String idColeccion,
                                                                         String categoria,
                                                                         String fechaReporteDesde,
                                                                         String fechaReporteHasta,
                                                                         String fechaAcontecimientoDesde,
                                                                         String fechaAcontecimientoHasta,
                                                                         Double latitud,
                                                                         Double longitud,
                                                                         Double radio,
                                                                         String search,
                                                                         Integer page,
                                                                         Integer size) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/colecciones/{idColeccion}/hechosCurados");
                    return getUri(idColeccion, categoria, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud, radio, search, page, size, uriBuilder);
                })
                .retrieve()
                .bodyToMono(GraphQLHechosCuradosResponse.class)
                .map(graphqlResponse -> {
                    if (graphqlResponse.getData() != null &&
                        graphqlResponse.getData().getHechosPorColeccion() != null) {

                        GraphQLHechosCuradosResponse.HechosWrapper wrapper =
                            graphqlResponse.getData().getHechosPorColeccion();
                        GraphQLHechosCuradosResponse.PageInfo pageInfo = wrapper.getPageInfo();

                        PageWrapper<HechoMapaOutputDto> pageWrapper = new PageWrapper<>();
                        pageWrapper.setContent(wrapper.getContent());
                        pageWrapper.setNumber(pageInfo.getNumber());
                        pageWrapper.setSize(pageInfo.getSize());
                        pageWrapper.setTotalElements(pageInfo.getTotalElements());
                        pageWrapper.setTotalPages(pageInfo.getTotalPages());
                        pageWrapper.setFirst(pageInfo.getNumber() == 0);
                        pageWrapper.setLast(!pageInfo.isHasNext());

                        return pageWrapper;
                    }
                    PageWrapper<HechoMapaOutputDto> emptyWrapper = new PageWrapper<>();
                    emptyWrapper.setContent(new java.util.ArrayList<>());
                    return emptyWrapper;
                })
                .doOnError(e -> System.err.println("Error al obtener hechos curados de la colección de la API Pública: " + e.getMessage()));
    }

    private URI getUri(String idColeccion, String categoria, String fechaReporteDesde, String fechaReporteHasta, String fechaAcontecimientoDesde, String fechaAcontecimientoHasta, Double latitud, Double longitud, Double radio, String search, Integer page, Integer size, UriBuilder uriBuilder) {
        if (categoria != null) uriBuilder.queryParam("categoria", categoria);
        if (fechaReporteDesde != null) uriBuilder.queryParam("fechaReporteDesde", fechaReporteDesde);
        if (fechaReporteHasta != null) uriBuilder.queryParam("fechaReporteHasta", fechaReporteHasta);
        if (fechaAcontecimientoDesde != null) uriBuilder.queryParam("fechaAcontecimientoDesde", fechaAcontecimientoDesde);
        if (fechaAcontecimientoHasta != null) uriBuilder.queryParam("fechaAcontecimientoHasta", fechaAcontecimientoHasta);
        if (latitud != null) uriBuilder.queryParam("latitud", latitud);
        if (longitud != null) uriBuilder.queryParam("longitud", longitud);
        if (radio != null) uriBuilder.queryParam("radio", radio);
        if (search != null) uriBuilder.queryParam("search", search);
        uriBuilder.queryParam("page", page);
        uriBuilder.queryParam("size", size);

        // Especificar los campos que queremos obtener de los hechos
        agregarCamposHechoAUri(uriBuilder);

        return uriBuilder.build(idColeccion);
    }

    /**
     * Método auxiliar para agregar los parámetros que especifican qué campos del Hecho queremos obtener.
     * Especificamos: id, titulo, latitud, longitud, categoria, fechaCarga
     */
    private void agregarCamposHechoAUri(UriBuilder uriBuilder) {
        // Campos que queremos: id, titulo, latitud, longitud, categoria, fechaCarga
        uriBuilder.queryParam("includeId", true);
        uriBuilder.queryParam("includeTitulo", true);
        uriBuilder.queryParam("includeDescripcion", false);
        uriBuilder.queryParam("includeLatitud", true);
        uriBuilder.queryParam("includeLongitud", true);
        uriBuilder.queryParam("includeCategoria", true);
        uriBuilder.queryParam("includeFechaCarga", true);
        uriBuilder.queryParam("includeFechaAcontecimiento", false);
        uriBuilder.queryParam("includeFechaUltimaModificacion", false);
        uriBuilder.queryParam("includeContenidoTexto", false);
        uriBuilder.queryParam("includeAnonimato", false);
    }

    public ColeccionOutputDto obtenerColeccion(String idColeccion) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/colecciones/{idColeccion}")
                        .build(idColeccion))
                .retrieve()
                .bodyToMono(ColeccionOutputDto.class)
                .doOnError(e -> System.err.println("Error al obtener la colección de la API Pública: " + e.getMessage()))
                .block();
    }
}