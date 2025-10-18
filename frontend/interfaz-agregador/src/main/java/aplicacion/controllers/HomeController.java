package aplicacion.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;


@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String paginaInicial(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        if (oidcUser != null) {
            boolean isAdmin = checkClaimForRole(oidcUser, "admin");
            model.addAttribute("botonDerechaFragment", "fragments/user-button :: botonPerfil");
            model.addAttribute("userName", oidcUser.getFullName());
            model.addAttribute("isAdmin", isAdmin);
        } else {
            model.addAttribute("botonDerechaFragment", "fragments/user-button :: botonLogin");
        }
        return "homepage";
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

    @GetMapping("/about")
    public String paginaAcercaDe(Model model) {
        model.addAttribute("botonDerechaFragment", "fragments/user-button :: botonVolver");
        return "about";
    }
}
