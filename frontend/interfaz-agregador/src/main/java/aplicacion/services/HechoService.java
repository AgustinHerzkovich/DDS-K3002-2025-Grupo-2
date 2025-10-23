package aplicacion.services;

import aplicacion.dto.output.HechoMapaOutputDto;
import aplicacion.dto.output.HechoOutputDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class HechoService {

    @Value("${api.publica.port}")
    private String apiPublicaPort;

    private WebClient webClient;

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

    // Metodo que devuelve un Flux de hechos
    public Flux<HechoMapaOutputDto> obtenerHechos() {
        return webClient.get()
                .uri("/hechos")
                .retrieve()
                .bodyToFlux(HechoMapaOutputDto.class)
                .doOnError(e -> System.err.println("Error al obtener hechos de la API Pública: " + e.getMessage()));
    }

    public HechoOutputDto obtenerHecho(String id) {
        return webClient.get()
                .uri("/hechos/{id}", id)
                .retrieve()
                .bodyToMono(HechoOutputDto.class)
                .doOnError(e -> System.err.println("Error al obtener hecho con ID " + id + " de la API Pública: " + e.getMessage()))
                .block();
    }

    public Flux<HechoMapaOutputDto> obtenerHechosConFiltros(
            String categoria,
            String fechaReporteDesde,
            String fechaReporteHasta,
            String fechaAcontecimientoDesde,
            String fechaAcontecimientoHasta,
            Double latitud,
            Double longitud,
            String search
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

                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToFlux(HechoMapaOutputDto.class)
                .doOnError(e -> System.err.println("Error al obtener hechos con filtros de la API Pública: " + e.getMessage()));
    }
}

