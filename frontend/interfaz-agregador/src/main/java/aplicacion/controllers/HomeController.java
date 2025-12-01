package aplicacion.controllers;

import aplicacion.config.TokenContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String paginaInicial(Model model) {
        TokenContext.addToken(model);
        return "homepage";
    }

    @GetMapping("/about")
    public String paginaAcercaDe(Model model) {
        TokenContext.addToken(model);
        return "about";
    }
}
