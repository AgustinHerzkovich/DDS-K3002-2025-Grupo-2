package aplicacion.services;

import aplicacion.dto.PageWrapper;
import aplicacion.dto.output.HechoMapaOutputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.dto.output.SolicitudOutputDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class HechoService {

    @Value("${api.publica.port}")
    private Integer apiPublicaPort;

    private WebClient webClient;

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
    }

    // Metodo que devuelve un Flux de hechos con direcciones calculadas
    public Flux<HechoMapaOutputDto> obtenerHechos(Integer page, Integer size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/hechos")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageWrapper<HechoMapaOutputDto>>() {})
                .flatMapMany(pageWrapper -> Flux.fromIterable(pageWrapper.getContent()))
                .flatMap(hecho -> {
                    // Si el hecho tiene ubicación, calcular la dirección
                    if (hecho.getUbicacion() != null &&
                        hecho.getUbicacion().getLatitud() != null &&
                        hecho.getUbicacion().getLongitud() != null) {

                        return geocodingService.obtenerDireccionCorta(
                                hecho.getUbicacion().getLatitud(),
                                hecho.getUbicacion().getLongitud()
                        ).map(direccion -> {
                            hecho.setDireccion(direccion);
                            return hecho;
                        });
                    }
                    // Si no tiene ubicación, usar valor por defecto
                    hecho.setDireccion("Sin ubicación");
                    return Flux.just(hecho);
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

    public Flux<HechoMapaOutputDto> obtenerHechosConFiltros(
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
                .bodyToMono(new ParameterizedTypeReference<PageWrapper<HechoMapaOutputDto>>() {})
                .flatMapMany(pageWrapper -> Flux.fromIterable(pageWrapper.getContent()))
                .flatMap(hecho -> {
                    // Calcular la dirección si tiene ubicación
                    if (hecho.getUbicacion() != null &&
                        hecho.getUbicacion().getLatitud() != null &&
                        hecho.getUbicacion().getLongitud() != null) {

                        return geocodingService.obtenerDireccionCorta(
                                hecho.getUbicacion().getLatitud(),
                                hecho.getUbicacion().getLongitud()
                        ).map(direccion -> {
                            hecho.setDireccion(direccion);
                            return hecho;
                        });
                    }
                    hecho.setDireccion("Sin ubicación");
                    return Flux.just(hecho);
                })
                .doOnError(e -> System.err.println("Error al obtener hechos con filtros de la API Pública: " + e.getMessage()));
    }
}