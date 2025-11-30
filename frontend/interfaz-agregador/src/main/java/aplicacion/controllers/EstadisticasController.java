package aplicacion.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EstadisticasController {
    @GetMapping({"/stats"})
    public String paginaEstadisticas(){
        return "estadisticas";
    }
}
