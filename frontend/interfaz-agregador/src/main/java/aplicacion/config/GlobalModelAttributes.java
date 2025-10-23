package aplicacion.config;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collection;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void addGlobalAttributes(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        // Setear isLoggedIn globalmente para todos los controllers
        model.addAttribute("isLoggedIn", oidcUser != null);

        // Si el usuario está loggeado, también agregar su nombre
        if (oidcUser != null) {
            model.addAttribute("userName", oidcUser.getFullName());
            model.addAttribute("userEmail", oidcUser.getEmail());
            model.addAttribute("isAdmin", checkClaimForRole(oidcUser, "admin"));
        }
    }

    private boolean checkClaimForRole(OidcUser oidcUser, String targetRole) {
        Object rolesClaim = oidcUser.getClaim("realm_access"); // Keycloak usa 'realm_access.roles'
        if (rolesClaim instanceof java.util.Map) {
            @SuppressWarnings("unchecked")
            Collection<String> roles = (Collection<String>) ((java.util.Map<String, Object>) rolesClaim).get("roles");
            if (roles != null) {
                return roles.stream().anyMatch(role -> role.equalsIgnoreCase(targetRole));
            }
        }
        return false;
    }
}

