package aplicacion.services;

import aplicacion.dto.PageWrapper;
import aplicacion.dto.output.SolicitudOutputDto;
import jakarta.annotation.PostConstruct;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class SolicitudService {
    private WebClient webClient;

    @Value("${api.administrativa.port}")
    private String apiAdministrativaPort;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:" + apiAdministrativaPort + "/apiAdministrativa")
                // aumento el buffer para respuestas grandes
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer ->
                                configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024) // 16MB
                        )
                        .build())
                .build();
    }

    public Flux<SolicitudOutputDto> obtenerSolicitudes(int page, int size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/solicitudes")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageWrapper<SolicitudOutputDto>>() {})
                .flatMapMany(pageWrapper -> Flux.fromIterable(pageWrapper.getContent()))
                .doOnError(e -> System.err.println("Error al obtener solicitudes de la API Administrativa: " + e.getMessage()));
    }
}
