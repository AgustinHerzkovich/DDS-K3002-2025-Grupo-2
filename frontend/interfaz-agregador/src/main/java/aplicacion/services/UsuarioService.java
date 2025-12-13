package aplicacion.services;

import aplicacion.config.ConfigService;
import aplicacion.dto.input.ContribuyenteInputDto;
import aplicacion.dto.input.IdentidadContribuyenteInputDto;
import aplicacion.dto.output.ContribuyenteOutputDto;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Service
public class UsuarioService {
    private WebClient webClient;
    private Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private final ConfigService configService;

    public UsuarioService(@Lazy ConfigService configService) {
        this.configService = configService;
    }

    @PostConstruct
    public void init() {
        this.webClient = WebClient.create(configService.getUrlApiPublica());
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
            logger.debug("Se registro un usuario en Metamapa");
        } catch (Exception e) {
            logger.error("Error al registrar el usuario en el backend: {}", e.getMessage());
        }

        return contribuyenteDevueltoDto;
    }

}
