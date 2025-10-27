package aplicacion.controllers;



import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;



import java.util.*;


@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String paginaInicial(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        if (oidcUser != null) {
            model.addAttribute("principal", oidcUser);


            boolean isAdmin = checkClaimForRole(oidcUser, "admin"); // Chequeamos el rol sin prefijo
            model.addAttribute("isAdmin", isAdmin);

        }
        return "homepage";
    }

    // Metodo de utilidad para verificar el claim
    private boolean checkClaimForRole(OidcUser oidcUser, String targetRole) {
        // 1. Obtener el claim "realm_roles" (donde Keycloak pone el rol)
        Object rolesClaim = oidcUser.getClaim("realm_roles");

        if (rolesClaim instanceof Collection<?> roles) {
            // 2. Comprobar si la lista contiene el rol (ignorando caso y prefijo)
            return roles.stream()
                    .map(Object::toString)
                    .anyMatch(role -> role.equalsIgnoreCase(targetRole));
        }
        return false;
    }



    @GetMapping("/editar-perfil")
    @PreAuthorize("isAuthenticated()")
    public String editarPerfilForm(@AuthenticationPrincipal OidcUser principal, Model model) {
        if (principal != null) {
            model.addAttribute("principal", principal);

            Map<String, Object> claims = principal.getClaims();
            Map<String, String> perfilUsuario = new HashMap<>();

            perfilUsuario.put("nombre", (String) claims.get("given_name"));
            perfilUsuario.put("apellido", (String) claims.get("family_name"));

            String fechaNacimientoClaim = principal.getClaimAsString("birthdate");
            perfilUsuario.put("fechaNacimiento", fechaNacimientoClaim);

            perfilUsuario.put("email", principal.getEmail());

            boolean isAdmin = checkClaimForRole(principal, "admin");
            model.addAttribute("isAdmin", isAdmin);

            model.addAttribute("usuario", perfilUsuario);

            return "editar-perfil";
        }
        return "redirect:/";
    }

    @GetMapping("/subir-hechos")
    public String showUploadForm(@AuthenticationPrincipal OidcUser principal, Model model) {
        if (principal != null) {
            model.addAttribute("principal", principal);

            boolean isAdmin = checkClaimForRole(principal, "admin");
            model.addAttribute("isAdmin", isAdmin);
        }
        return "subir-hechos";
    }

    @PostMapping("/save-redirect-url")
    @ResponseBody
    public String saveRedirectUrl(@RequestParam String url, HttpServletRequest request) {
        request.getSession().setAttribute("REDIRECT_URL_AFTER_LOGIN", url);
        return "OK";
    }



    @GetMapping("/check-roles")
    @ResponseBody
    public String checkRoles(@AuthenticationPrincipal OidcUser principal) {
        StringBuilder sb = new StringBuilder();
        sb.append("Roles del usuario:\n");
        for (GrantedAuthority auth : principal.getAuthorities()) {
            sb.append(auth.getAuthority()).append("\n");
        }
        return sb.toString();
    }

    //estar protegido para que Spring Security lo intercepte y renueve el OidcUser, y luego redirigir a la vista de perfil.
    @GetMapping("/perfil-actualizado-exito")
    @PreAuthorize("isAuthenticated()")
    public String handleProfileUpdateRedirect() {
        // Al entrar a esta URL protegida, Spring Security OIDC intentará
        // refrescar el token/principal.

        // Luego, redirigimos a la página donde el usuario debe ver los datos actualizados.
        return "redirect:/editar-perfil?success=true";
    }
}




