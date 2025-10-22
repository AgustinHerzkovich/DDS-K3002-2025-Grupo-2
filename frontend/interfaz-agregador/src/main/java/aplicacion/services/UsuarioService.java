package aplicacion.services;

import aplicacion.dto.input.ContribuyenteInputDto;
import aplicacion.dto.input.IdentidadContribuyenteInputDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

@Service
public class UsuarioService {
    private WebClient webClient;

    @Value("${api.publica.port}")
    private Integer apiPublicaPort;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.create("http://localhost:" + apiPublicaPort + "/apiPublica");
    }

    public void registrarUsuarioSiNoExiste(OidcUser oidcUser) {
        IdentidadContribuyenteInputDto identidad = new IdentidadContribuyenteInputDto(oidcUser.getClaimAsString("given_name"),
                                                                                      oidcUser.getClaimAsString("family_name"),
                                                                                      LocalDate.parse(oidcUser.getClaimAsString("birthdate")));
        boolean esAdmin = false;
        Object rolesClaim = oidcUser.getClaim("realm_roles");
        if (rolesClaim instanceof Collection<?> roles) {
            esAdmin = roles.stream()
                    .map(Object::toString)
                    .anyMatch(role -> role.equalsIgnoreCase("admin"));
        }

        ContribuyenteInputDto contribuyente = new ContribuyenteInputDto(esAdmin, identidad, oidcUser.getEmail());

        // Llamamos a un endpoint POST en nuestro backend para crear/verificar el usuario
        try {
            webClient.post()
                    .uri("/contribuyentes")
                    .bodyValue(contribuyente)
                    .retrieve()
                    .toBodilessEntity()
                    .block(); // Usamos block() para simplicidad
        } catch (Exception e) {
            System.err.println("Error al registrar el usuario en el backend: " + e.getMessage());
        }
    }
}
