package aplicacion.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

@Controller
public class HomeController {
    @GetMapping({"/", "/home"})
    public String paginaInicial(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        // Determinar qué botón mostrar según si el usuario está autenticado
        if (oidcUser != null) {
            // Usuario autenticado - mostrar botón de perfil
            model.addAttribute("botonDerechaFragment", "fragments/user-button :: botonPerfil");
            model.addAttribute("userName", oidcUser.getFullName());
        } else {
            // Usuario no autenticado - mostrar botón de login
            model.addAttribute("botonDerechaFragment", "fragments/user-button :: botonLogin");
        }

        return "homepage";
    }

    @GetMapping("/about")
    public String paginaAcercaDe(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        // Para la página About siempre mostramos el botón de volver
        model.addAttribute("botonDerechaFragment", "fragments/user-button :: botonVolver");
        return "about";
    }

    @GetMapping("/login")
    public String paginaLogin() {
        return "login";
    }

    @GetMapping("/profile")
    @ResponseBody
    public String paginaProtegida(@AuthenticationPrincipal OidcUser oidcUser) {
        String nombre = oidcUser.getFullName();
        String email = oidcUser.getEmail();
        return "¡Login exitoso! Bienvenido, " + nombre + "! Tu email es: " + email + ".";
    }

    @PostMapping("/save-redirect-url")
    @ResponseBody
    public String saveRedirectUrl(@RequestParam String url, HttpServletRequest request) {
        // Guardar la URL en la sesión del servidor
        request.getSession().setAttribute("REDIRECT_URL_AFTER_LOGIN", url);
        return "OK";
    }
}
