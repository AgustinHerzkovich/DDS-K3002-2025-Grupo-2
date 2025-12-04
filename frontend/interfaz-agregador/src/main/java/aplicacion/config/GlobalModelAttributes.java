package aplicacion.config;

import aplicacion.dto.output.ContribuyenteOutputDto;
import aplicacion.services.ContribuyenteService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    private final ContribuyenteService contribuyenteService;

    private static final Logger log = LoggerFactory.getLogger(GlobalModelAttributes.class);

    public GlobalModelAttributes(ContribuyenteService contribuyenteService) {
        this.contribuyenteService = contribuyenteService;
    }

    @ModelAttribute
    public void addGlobalAttributes(HttpServletRequest request, @AuthenticationPrincipal OidcUser oidcUser, Model model) {
        String currentUri = request.getRequestURI();
        model.addAttribute("currentUri", currentUri);
        // Setear isLoggedIn globalmente para todos los controllers
        model.addAttribute("isLoggedIn", oidcUser != null);

        // Si el usuario está loggeado, también agregar su nombre
        if (oidcUser != null) {
            model.addAttribute("userName", oidcUser.getFullName());
            model.addAttribute("userEmail", oidcUser.getEmail());

            // Verificar si tiene rol ADMIN usando las authorities de Spring Security
            boolean isAdmin = oidcUser.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin);

            ContribuyenteOutputDto contribuyente = contribuyenteService.obtenerContribuyentePorMail(oidcUser);
            if (contribuyente != null) {
                model.addAttribute("userId", contribuyente.getId()); // ID del usuario
            } else {
                model.addAttribute("userId", null);
            }

            // Variables adicionales para el modal de crear hecho
            model.addAttribute("userKeycloakId", oidcUser.getSubject()); // ID de Keycloak
            model.addAttribute("email", oidcUser.getEmail());
            model.addAttribute("firstName", oidcUser.getGivenName());
            model.addAttribute("lastName", oidcUser.getFamilyName());
            model.addAttribute("birthDate", oidcUser.getClaimAsString("birthdate"));
        } else {
            // Si no está loggeado, setear valores null
            model.addAttribute("userKeycloakId", null);
            model.addAttribute("isAdmin", false);
            model.addAttribute("userId", null);
            model.addAttribute("email", null);
            model.addAttribute("firstName", null);
            model.addAttribute("lastName", null);
            model.addAttribute("birthDate", null);
        }
    }
}

