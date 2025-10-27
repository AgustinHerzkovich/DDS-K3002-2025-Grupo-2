package aplicacion.services;

import aplicacion.dto.output.ContribuyenteOutputDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ContribuyenteService {
    private WebClient webClient;

    @Value("${api.publica.port}")
    private Integer apiPublicaPort;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:" + apiPublicaPort + "/apiPublica")
                .build();
    }

    public ContribuyenteOutputDto obtenerContribuyentePorMail(String mail) {
        try {
            ContribuyenteOutputDto contribuyente = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/contribuyentes")
                            .queryParam("mail", mail)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ContribuyenteOutputDto>() {})
                    .block();

            if (contribuyente != null) {
                return contribuyente;
            } else {
                System.err.println("No se encontro contribuyente con el email: " + mail);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error al obtener contribuyente por mail: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

