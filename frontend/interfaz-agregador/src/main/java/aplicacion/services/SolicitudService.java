package aplicacion.services;

import aplicacion.controllers.HechoController;
import aplicacion.dto.PageWrapper;
import aplicacion.dto.output.SolicitudOutputDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class SolicitudService {
    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(HechoController.class);

    // Inyectar específicamente el bean apiAdministrativaWebClient que tiene el filtro
    public SolicitudService(WebClient apiAdministrativaWebClient) {
        this.webClient = apiAdministrativaWebClient;
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
                .doOnError(e -> logger.error("Error al obtener solicitudes de la API Administrativa: {}", e.getMessage()));
    }
}
