package aplicacion.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String paginaInicial() {
        return "homepage";
    }

    @GetMapping("/protegido")
    @ResponseBody
    public String paginaProtegida(@AuthenticationPrincipal OidcUser oidcUser) {
        String nombre = oidcUser.getFullName();
        String email = oidcUser.getEmail();
        return "¡Login exitoso! Bienvenido, " + nombre + "! Tu email es: " + email + ".";
    }
}
