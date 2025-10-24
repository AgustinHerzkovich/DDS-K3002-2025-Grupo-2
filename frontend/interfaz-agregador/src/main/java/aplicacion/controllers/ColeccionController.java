package aplicacion.controllers;

import org.springframework.ui.Model;
import aplicacion.dto.output.ColeccionOutputDto;
import aplicacion.services.ColeccionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;


@Controller
public class ColeccionController {
    private final ColeccionService coleccionService;

    public ColeccionController(ColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @GetMapping("/colecciones")
    public String paginaColecciones(@RequestParam(name = "search", required = false) String search,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    Model model) {
        List<ColeccionOutputDto> colecciones = coleccionService
                .obtenerColecciones(page, size, search)
                .collectList()
                .block();

        model.addAttribute("colecciones", colecciones);
        return "colecciones";
    }
}