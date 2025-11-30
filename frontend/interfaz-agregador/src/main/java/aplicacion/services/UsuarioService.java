package aplicacion.services;

import aplicacion.dto.input.ContribuyenteInputDto;
import aplicacion.dto.input.IdentidadContribuyenteInputDto;
import aplicacion.dto.output.ContribuyenteOutputDto;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
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

    public ContribuyenteOutputDto registrarUsuarioSiNoExiste(OidcUser oidcUser) {
        Optional<LocalDate> fechaNacimiento;
        ContribuyenteOutputDto contribuyenteDevueltoDto = null;
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

        // Variable para almacenar el ID después del registro
        String contribuyenteId = oidcUser.getSubject();

        ContribuyenteInputDto contribuyente = new ContribuyenteInputDto(contribuyenteId, esAdmin, identidad, oidcUser.getEmail());

        // 1. LLAMADA A API PÚBLICA (webClient)
        try {
            // Esperamos que el backend devuelva el objeto ContribuyenteOutputDto
            contribuyenteDevueltoDto = webClient.post()
                    .uri("/contribuyentes")
                    .bodyValue(contribuyente)
                    .retrieve()
                    .bodyToMono(ContribuyenteOutputDto.class) // <--- Obtiene el cuerpo de la respuesta
                    .block();
            System.out.println("Se registro al usuario en Agregador");
        } catch (Exception e) {
            System.err.println("Error al registrar el usuario en el backend: " + e.getMessage());
        }

        // 3. LLAMADA A FUENTES DINÁMICAS (usando el mismo DTO de entrada)
        try {
            fuentesDinamicasWebClient.post()
                    .uri("/contribuyentes")
                    .bodyValue(contribuyente) // Usamos el mismo DTO de entrada (ContribuyenteInputDto)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            System.out.println("Se registro el usuario en fuenteDinamica");
        } catch (Exception e) {
            System.err.println("Error al registrar el usuario en Fuentes Dinámicas: " + e.getMessage());
        }
        return contribuyenteDevueltoDto;
    }

}
