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
    @GetMapping({"/stats/colecciones"})
    public String paginaEstadisticasColecciones(Model model){
        TokenContext.addToken(model);
        return "estadisticasColecciones";
    }
    @GetMapping({"/stats/categorias"})
    public String paginaEstadisticasCategorias(Model model){
        TokenContext.addToken(model);
        return "estadisticasCategorias";
    }
    @GetMapping({"/stats/solicitudes"})
    public String paginaEstadisticasSolicitudes(Model model){
        TokenContext.addToken(model);
        return "estadisticasSolicitudes";
    }
}
