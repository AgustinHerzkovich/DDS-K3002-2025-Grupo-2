package aplicacion.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // <-- Asegúrate de que este import exista
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HomeController {


    @GetMapping({"/", "/home"})
    public String paginaInicial(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        // Pasa el objeto oidcUser (que puede ser null si no está autenticado) al modelo.
        // Ahora Thymeleaf puede usar la variable 'principal'.
        model.addAttribute("principal", oidcUser);
        return "homepage";
    }

    @GetMapping("/subir-hechos")
    public String showUploadForm() {
        return "subir-hechos";
    }
    /*
    @GetMapping("/mis-hechos")
    public String misHechos(@AuthenticationPrincipal OidcUser principal, Model model) {
        if (principal != null) {
            model.addAttribute("principal", principal);
            // Obtener los hechos del usuario actual

            model.addAttribute("hechos", hechosUsuario);
            return "mis-hechos";
        }
        return "redirect:/";
    }
    */

    @PostMapping("/save-redirect-url")
    @ResponseBody
    public String saveRedirectUrl(@RequestParam String url, HttpServletRequest request) {
        request.getSession().setAttribute("REDIRECT_URL_AFTER_LOGIN", url);
        return "OK";
    }
}