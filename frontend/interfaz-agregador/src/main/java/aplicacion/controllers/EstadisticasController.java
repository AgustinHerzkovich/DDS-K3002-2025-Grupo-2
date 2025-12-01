package aplicacion.controllers;

import aplicacion.config.TokenContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EstadisticasController {
    @GetMapping({"/stats"})
    public String paginaEstadisticas(Model model){
        TokenContext.addToken(model);
        return "estadisticas";
    }
}
