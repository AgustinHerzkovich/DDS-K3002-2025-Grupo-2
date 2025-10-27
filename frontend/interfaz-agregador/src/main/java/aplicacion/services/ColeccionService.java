package aplicacion.services;

import aplicacion.dto.PageWrapper;
import aplicacion.dto.output.ColeccionOutputDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

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

    public Flux<ColeccionOutputDto> obtenerColecciones(int page, int size, String search) {
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
                .flatMapMany(pageWrapper -> Flux.fromIterable(pageWrapper.getContent()))
                .doOnError(e -> System.err.println("Error al obtener colecciones de la API Pública: " + e.getMessage()));
    }
}