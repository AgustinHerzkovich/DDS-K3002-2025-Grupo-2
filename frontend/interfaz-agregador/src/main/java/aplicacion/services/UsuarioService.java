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
import java.util.Optional;

@Service
public class UsuarioService {
    private WebClient webClient;
    private WebClient fuentesDinamicasWebClient;
    @Value("${api.publica.port}")
    private Integer apiPublicaPort;
    @Value("8082")
    private String fuenteDinamicaPort;
    @PostConstruct
    public void init() {

        this.webClient = WebClient.create("http://localhost:" + apiPublicaPort + "/apiPublica");
        this.fuentesDinamicasWebClient = WebClient.create("http://localhost:" + fuenteDinamicaPort + "/fuentesDinamicas");
    }

    public void registrarUsuarioSiNoExiste(OidcUser oidcUser) {
        Optional<LocalDate> fechaNacimiento;
        try{
            fechaNacimiento = Optional.of(LocalDate.parse(oidcUser.getClaimAsString("birthdate")));
        }catch (Exception ignored){
            fechaNacimiento = Optional.empty();
        }
        IdentidadContribuyenteInputDto identidad = new IdentidadContribuyenteInputDto(oidcUser.getClaimAsString("given_name"),
                                                                                      oidcUser.getClaimAsString("family_name"),
                                                                                      fechaNacimiento.orElse(null));
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

        try {
            fuentesDinamicasWebClient.post()
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
