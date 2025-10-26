package aplicacion.services;

import aplicacion.dto.output.ContribuyenteOutputDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ContribuyenteService {
    private final WebClient webClient;

    public ContribuyenteService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8085").build();
    }

    public ContribuyenteOutputDto obtenerContribuyentePorMail(String mail) {
        try {
            String url = "/apiPublica/contribuyentes?mail=" + mail;

            // La API devuelve una lista, no un solo objeto
            List<ContribuyenteOutputDto> contribuyentes = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<ContribuyenteOutputDto>>() {})
                    .block();

            if (contribuyentes != null && !contribuyentes.isEmpty()) {
                ContribuyenteOutputDto contribuyente = contribuyentes.get(0);
                return contribuyente;
            } else {
                System.err.println("No se encontraron contribuyentes con el email: " + mail);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error al obtener contribuyente por mail: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

