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
    public String paginaInicial() {
        return "homepage";
    }

    @GetMapping("/about")
    public String paginaAcercaDe() {
        return "about";
    }
}
