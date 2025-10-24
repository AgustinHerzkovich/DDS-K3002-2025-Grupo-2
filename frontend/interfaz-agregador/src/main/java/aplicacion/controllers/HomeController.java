package aplicacion.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



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
