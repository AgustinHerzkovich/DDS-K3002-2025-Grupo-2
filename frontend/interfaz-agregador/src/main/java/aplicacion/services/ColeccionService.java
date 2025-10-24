package aplicacion.services;

import aplicacion.dto.output.ColeccionOutputDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class ColeccionService {
    private WebClient webClient;

    @Value("${api.publica.port}")
    private String apiPublicaPort;

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
                .uri("/colecciones?search={search}&page={page}&size={size}", search, page, size)
                .retrieve()
                .bodyToFlux(ColeccionOutputDto.class)
                .doOnError(e -> System.err.println("Error al obtener colecciones de la API Agregador: " + e.getMessage()));
    }
}
