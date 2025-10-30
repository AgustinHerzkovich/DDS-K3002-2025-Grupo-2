package aplicacion.controllers;

import aplicacion.dto.output.HechoOutputDto;
import aplicacion.services.HechoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HechoController {
    private final HechoService hechoService;

    public HechoController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping("/hechos/{id}")
    public String paginaHecho(@PathVariable("id") String id,
                             Model model) {
        HechoOutputDto hecho = hechoService.obtenerHecho(id);
        if (hecho == null) {
            return "error/404"; // Ver si está bien tirar esto o capaz convenga otra cosa
        }

        model.addAttribute("hecho", hecho);

        return "hecho";
    }
}
