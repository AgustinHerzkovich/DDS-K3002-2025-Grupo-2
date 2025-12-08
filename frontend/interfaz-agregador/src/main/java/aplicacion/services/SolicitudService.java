package aplicacion.services;

import aplicacion.dto.PageWrapper;
import aplicacion.dto.output.SolicitudOutputDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class SolicitudService {
    private final WebClient webClient;

    public SolicitudService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<PageWrapper<SolicitudOutputDto>> obtenerSolicitudes(int page, int size) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/solicitudes")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageWrapper<SolicitudOutputDto>>() {})
                .doOnError(e -> System.err.println("Error al obtener solicitudes de la API Administrativa: " + e.getMessage()));
    }
}
