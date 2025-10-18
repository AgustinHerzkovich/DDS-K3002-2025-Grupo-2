package aplicacion.config;

import aplicacion.dtos.ContribuyenteInputDto;
import aplicacion.dtos.IdentidadContribuyenteInputDto;
import aplicacion.dtos.output.ContribuyenteOutputDto; // Clase que contiene el ID de la base de datos
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired; // Necesario si no usas @Component
import org.springframework.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;


@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RestTemplate restTemplate;
    private static final String CONTRIBUYENTE_API_URL = "http://localhost:8082/fuentesDinamicas/contribuyentes";

    // 💡 Constructor para inyectar RestTemplate
    @Autowired
    public CustomAuthenticationSuccessHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //  Envía la solicitud POST y guarda el ID en la sesión

    private void sendContribuyenteCreationRequest(OidcUser oidcUser, HttpServletRequest request) {
        try {
            // 1. Crear el DTO con los datos del usuario autenticado
            ContribuyenteInputDto dto = createContribuyenteDto(oidcUser);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ContribuyenteInputDto> requestEntity = new HttpEntity<>(dto, headers);
            // 2. Ejecutar POST y capturar la respuesta (que contiene el ID)
            ResponseEntity<ContribuyenteOutputDto> response = restTemplate.exchange(
                    CONTRIBUYENTE_API_URL,
                    HttpMethod.POST,
                    requestEntity,
                    ContribuyenteOutputDto.class // Captura el DTO de salida
            );
            // Verificar éxito y guardar ID
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Long contribuyenteId = response.getBody().getId();
                //  Guardar el ID en la Sesión HTTP para usarlo después (ej. para subir hechos)
                request.getSession().setAttribute("CONTRIBUYENTE_ID", contribuyenteId);
                System.out.println("Contribuyente ID guardado en sesión: " + contribuyenteId);
            }
        } catch (Exception e) {
            System.err.println("ERROR al enviar POST para crear Contribuyente: " + e.getMessage());
        }
    }

   // mapear OidcUser a ContribuyenteInputDto

    private ContribuyenteInputDto createContribuyenteDto(OidcUser oidcUser) {
        // Asumiendo que los DTOs tienen @Setter o son mutables.
        ContribuyenteInputDto contribuyenteDto = new ContribuyenteInputDto();

        // Extracción de datos de los claims de Keycloak
        String nombre = oidcUser.getClaimAsString("given_name");
        String apellido = oidcUser.getClaimAsString("family_name");
        String mail = oidcUser.getEmail();
        String fechaNacimientoStr = oidcUser.getClaimAsString("birthdate");

        LocalDate fechaNacimiento = (fechaNacimientoStr != null) ? LocalDate.parse(fechaNacimientoStr) : null;

        // Lógica de verificación de rol de administrador (usando el claim directo)
        boolean esAdmin = false;
        Object rolesClaim = oidcUser.getClaim("realm_roles");
        if (rolesClaim instanceof Collection<?> roles) {
            esAdmin = roles.stream()
                    .map(Object::toString)
                    .anyMatch(role -> role.equalsIgnoreCase("admin"));
        }

        // 1. Construir y rellenar el DTO de Identidad
        IdentidadContribuyenteInputDto identidad = new IdentidadContribuyenteInputDto();

        identidad.setNombre(nombre);
        identidad.setApellido(apellido);
        identidad.setFechaNacimiento(fechaNacimiento);

        // 2. Rellenar el DTO principal
        contribuyenteDto.setMail(mail);
        contribuyenteDto.setEsAdministrador(esAdmin);
        contribuyenteDto.setIdentidad(identidad);

        return contribuyenteDto;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        System.out.println("auth " );
        // 1. Si la autenticación fue exitosa y es de tipo OAuth2/OIDC
        if (authentication instanceof OAuth2AuthenticationToken) {
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            // Ejecutar la lógica de creación de contribuyente y guardar el ID
            sendContribuyenteCreationRequest(oidcUser, request);
        }

        // 2. Lógica de Redirección (Tu código original)
        String targetUrl = (String) request.getSession().getAttribute("REDIRECT_URL_AFTER_LOGIN");

        if (targetUrl == null || targetUrl.isEmpty()) {
            targetUrl = "/";
        }

        request.getSession().removeAttribute("REDIRECT_URL_AFTER_LOGIN");
        response.sendRedirect(targetUrl);
    }
}