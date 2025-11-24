package aplicacion.services;

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
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Service
public class HechoService {
    @Value("8082")
    private String fuenteDinamicaPort;

    @Value("${api.publica.port}")
    private Integer apiPublicaPort;
    private final String FUENTE_DINAMICA_URL = "http://localhost:8082/fuentesDinamicas/hechos";

    private WebClient webClient;
    private WebClient fuentesDinamicasWebClient;

    private final GeocodingService geocodingService;

    public HechoService(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:" + apiPublicaPort + "/apiPublica")
                // aumento el buffer para respuestas grandes
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer ->
                                configurer.defaultCodecs().maxInMemorySize(20 * 1024 * 1024) // 20MB
                        )
                        .build())
                .build();
        this.fuentesDinamicasWebClient = WebClient.create("http://localhost:" + fuenteDinamicaPort + "/fuentesDinamicas");
    }

    // Metodo que devuelve un Flux de hechos con direcciones calculadas
    public Mono<PageWrapper<HechoMapaOutputDto>> obtenerHechos(Integer page, Integer size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/hechos")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToMono(aplicacion.dto.GraphQLHechosResponse.class)
                .<PageWrapper<HechoMapaOutputDto>>map(graphqlResponse -> {
                    if (graphqlResponse.getData() != null &&
                        graphqlResponse.getData().getHechosEnMapa() != null) {

                        aplicacion.dto.GraphQLHechosResponse.HechosEnMapaWrapper wrapper =
                            graphqlResponse.getData().getHechosEnMapa();
                        aplicacion.dto.GraphQLHechosResponse.PageInfo pageInfo = wrapper.getPageInfo();

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
                .doOnError(e -> System.err.println("Error al obtener hechos de la API Pública: " + e.getMessage()));
    }

    public HechoOutputDto obtenerHecho(String id) {
        HechoOutputDto hecho = webClient.get()
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
            ).block(); // Bloqueamos para obtener el resultado

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
            String search,
            Integer page,
            Integer size
    ) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/hechos");

                    if (categoria != null && !categoria.isEmpty()) uriBuilder.queryParam("categoria", categoria);
                    if (fechaReporteDesde != null && !fechaReporteDesde.isEmpty()) uriBuilder.queryParam("fechaReporteDesde", fechaReporteDesde);
                    if (fechaReporteHasta != null && !fechaReporteHasta.isEmpty()) uriBuilder.queryParam("fechaReporteHasta", fechaReporteHasta);
                    if (fechaAcontecimientoDesde != null && !fechaAcontecimientoDesde.isEmpty()) uriBuilder.queryParam("fechaAcontecimientoDesde", fechaAcontecimientoDesde);
                    if (fechaAcontecimientoHasta != null && !fechaAcontecimientoHasta.isEmpty()) uriBuilder.queryParam("fechaAcontecimientoHasta", fechaAcontecimientoHasta);
                    if (latitud != null) uriBuilder.queryParam("latitud", latitud);
                    if (longitud != null) uriBuilder.queryParam("longitud", longitud);
                    if (search != null && !search.isEmpty()) uriBuilder.queryParam("search", search);

                    uriBuilder.queryParam("page", page);
                    uriBuilder.queryParam("size", size);

                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(aplicacion.dto.GraphQLHechosResponse.class)
                .<PageWrapper<HechoMapaOutputDto>>map(graphqlResponse -> {
                    if (graphqlResponse.getData() != null &&
                        graphqlResponse.getData().getHechosEnMapa() != null) {

                        aplicacion.dto.GraphQLHechosResponse.HechosEnMapaWrapper wrapper =
                            graphqlResponse.getData().getHechosEnMapa();
                        aplicacion.dto.GraphQLHechosResponse.PageInfo pageInfo = wrapper.getPageInfo();

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



    public List<HechoOutputDto> obtenerHechosPendientes() {
        try {
            List<HechoOutputDto> lista = fuentesDinamicasWebClient.get()
                    .uri("/hechos?pendiente=true")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), resp ->
                            resp.bodyToMono(String.class).flatMap(msg -> {
                                System.err.println("Error 4xx en hechosPendientes pendientes: " + msg);
                                return Mono.error(new RuntimeException(msg));
                            })
                    )
                    .bodyToFlux(HechoOutputDto.class)
                    .collectList()
                    .block(Duration.ofSeconds(10));

            if (lista == null) {
                return Collections.emptyList();
            }
            System.out.println("Servicio: Cargadas " + lista.size() + " hechosPendientes pendientes.");
            return lista;
        } catch (WebClientResponseException e) {
            System.err.println("ERROR WebClient al obtener hechosPendientes pendientes: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("ERROR al obtener hechosPendientes pendientes: " + e.getMessage());
        }

        return Collections.emptyList();
    }


    public ResponseEntity<String> gestionarRevision(String hechoId, CambioEstadoRevisionInputDto cambioEstadoRevisionInputDto) throws HttpClientErrorException {

        try {
            ResponseEntity<String> response = fuentesDinamicasWebClient.patch() // USAR fuentesDinamicasWebClient
                    .uri("/hechos/{hechoId}/estadoRevision", hechoId) // URI relativo
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
}