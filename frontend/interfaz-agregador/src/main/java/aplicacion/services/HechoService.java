package aplicacion.services;

import aplicacion.config.TokenContext;
import aplicacion.dto.GraphQLHechosResponse;
import aplicacion.dto.PageWrapper;
import aplicacion.dto.output.HechoMapaOutputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.dto.input.CambioEstadoRevisionInputDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;

@Service
public class HechoService {
    @Value("${api.publica.url}")
    private String apiPublicaUrl;

    private WebClient webClientPublica;

    @Value("${api.administrativa.url}")
    private String apiAdministrativaUrl;

    private WebClient webClientAdministrativa;

    private final GeocodingService geocodingService;

    public HechoService(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @PostConstruct
    public void init() {
        this.webClientPublica = WebClient.builder()
                .baseUrl(apiPublicaUrl)
                // aumento el buffer para respuestas grandes
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer ->
                                configurer.defaultCodecs().maxInMemorySize(20 * 1024 * 1024) // 20MB
                        )
                        .build())
                .build();
        this.webClientAdministrativa = WebClient.builder()
                .baseUrl(apiAdministrativaUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer ->
                                configurer.defaultCodecs().maxInMemorySize(20 * 1024 * 1024) // 20MB
                        )
                        .build())
                .build();
    }

    // Metodo que devuelve un Flux de hechos con direcciones calculadas
    public Mono<PageWrapper<HechoMapaOutputDto>> obtenerHechos(Integer page, Integer size) {
        return webClientPublica.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/hechos")
                            .queryParam("page", page)
                            .queryParam("size", size);
                    // Especificar los campos que queremos obtener
                    agregarCamposHechoAUri(uriBuilder);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(GraphQLHechosResponse.class)
                .map(graphqlResponse -> {
                    if (graphqlResponse.getData() != null &&
                        graphqlResponse.getData().getHechosEnMapa() != null) {

                        GraphQLHechosResponse.HechosEnMapaWrapper wrapper =
                            graphqlResponse.getData().getHechosEnMapa();
                        GraphQLHechosResponse.PageInfo pageInfo = wrapper.getPageInfo();

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
                    emptyWrapper.setContent(new ArrayList<>());
                    return emptyWrapper;
                })
                .doOnError(e -> System.err.println("Error al obtener hechos de la API Pública: " + e.getMessage()));
    }

    public HechoOutputDto obtenerHecho(String id) {
        HechoOutputDto hecho = webClientPublica.get()
                .uri("/hechos/{id}", id)
                .retrieve()
                .bodyToMono(HechoOutputDto.class)
                .doOnError(e -> System.err.println("Error al obtener hecho con ID " + id + " de la API Pública: " + e.getMessage()))
                .block();

        // Calcular la dirección completa si tiene ubicación
        if (hecho != null && hecho.getUbicacion() != null &&
            hecho.getUbicacion().getLatitud() != null &&
            hecho.getUbicacion().getLongitud() != null) {

            String direccion = geocodingService.obtenerDireccion(
                    hecho.getUbicacion().getLatitud(),
                    hecho.getUbicacion().getLongitud()
            ); // Bloqueamos para obtener el resultado

            hecho.setDireccion(direccion);
        } else if (hecho != null) {
            hecho.setDireccion("Sin ubicación");
        }

        return hecho;
    }

    public Mono<PageWrapper<HechoMapaOutputDto>> obtenerHechosConFiltros(
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
            Integer size
    ) {
        return webClientPublica.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/hechos");

                    if (categoria != null && !categoria.isEmpty()) uriBuilder.queryParam("categoria", categoria);
                    if (fechaReporteDesde != null && !fechaReporteDesde.isEmpty()) uriBuilder.queryParam("fechaReporteDesde", fechaReporteDesde);
                    if (fechaReporteHasta != null && !fechaReporteHasta.isEmpty()) uriBuilder.queryParam("fechaReporteHasta", fechaReporteHasta);
                    if (fechaAcontecimientoDesde != null && !fechaAcontecimientoDesde.isEmpty()) uriBuilder.queryParam("fechaAcontecimientoDesde", fechaAcontecimientoDesde);
                    if (fechaAcontecimientoHasta != null && !fechaAcontecimientoHasta.isEmpty()) uriBuilder.queryParam("fechaAcontecimientoHasta", fechaAcontecimientoHasta);
                    if (latitud != null) uriBuilder.queryParam("latitud", latitud);
                    if (longitud != null) uriBuilder.queryParam("longitud", longitud);
                    if (radio != null) uriBuilder.queryParam("radio", radio);
                    if (search != null && !search.isEmpty()) uriBuilder.queryParam("search", search);

                    uriBuilder.queryParam("page", page);
                    uriBuilder.queryParam("size", size);

                    // Especificar los campos que queremos obtener
                    agregarCamposHechoAUri(uriBuilder);

                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(GraphQLHechosResponse.class)
                .map(graphqlResponse -> {
                    if (graphqlResponse.getData() != null &&
                        graphqlResponse.getData().getHechosEnMapa() != null) {

                        GraphQLHechosResponse.HechosEnMapaWrapper wrapper =
                            graphqlResponse.getData().getHechosEnMapa();
                        GraphQLHechosResponse.PageInfo pageInfo = wrapper.getPageInfo();

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
                .doOnError(e -> System.err.println("Error al obtener hechos con filtros de la API Pública: " + e.getMessage()));
    }



    public PageWrapper<HechoOutputDto> obtenerHechosPendientes(int page, int size) {
        try {
            String token = TokenContext.getToken();
            PageWrapper<HechoOutputDto> pageWrapper = webClientAdministrativa.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/hechos")
                            .queryParam("pendiente", true)
                            .queryParam("page", page)
                            .queryParam("size", size)
                            .build())
                    .header("Authorization", "Bearer " + token)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), resp ->
                            resp.bodyToMono(String.class).flatMap(msg -> {
                                System.err.println("Error 4xx en hechos pendientes: " + msg);
                                return Mono.error(new RuntimeException(msg));
                            })
                    )
                    .bodyToMono(new ParameterizedTypeReference<PageWrapper<HechoOutputDto>>() {})
                    .block(Duration.ofSeconds(30));

            if (pageWrapper == null || pageWrapper.getContent() == null) {
                return new PageWrapper<>();
            }
            System.out.println("Servicio: Cargados " + pageWrapper.getContent().size() + " hechos pendientes de " + pageWrapper.getTotalElements() + " totales.");
            return pageWrapper;
        } catch (WebClientResponseException e) {
            System.err.println("ERROR WebClient al obtener hechos pendientes: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw e; // Se propaga al errorhandler
        } catch (Exception e) {
            System.err.println("ERROR al obtener hechos pendientes: " + e.getMessage());
        }

        return new PageWrapper<>();
    }


    public ResponseEntity<String> gestionarRevision(String hechoId, CambioEstadoRevisionInputDto cambioEstadoRevisionInputDto) throws HttpClientErrorException {

        try {
            String token = TokenContext.getToken();
            ResponseEntity<String> response = webClientAdministrativa.patch() // USAR webClientAdministrativa
                    .uri("/hechos/{hechoId}/estadoRevision", hechoId) // URI relativo
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(cambioEstadoRevisionInputDto)
                    .retrieve()
                    .toEntity(String.class)
                    .block(Duration.ofSeconds(10));

            return response != null ? response : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Sin respuesta\"}");
        } catch (WebClientResponseException e) {

            throw new HttpClientErrorException(e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("ERROR al gestionar revision para ID " + hechoId + ": " + e.getMessage());
            throw e;
        }
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
}