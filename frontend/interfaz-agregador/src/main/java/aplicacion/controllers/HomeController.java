package aplicacion.controllers;

import aplicacion.config.TokenContext;
import aplicacion.dto.PageWrapper;
import aplicacion.dto.output.HechoMapaOutputDto;
import aplicacion.services.GeocodingService;
import aplicacion.services.HechoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class HomeController {
    private final HechoService hechoService;
    private final GeocodingService geocodingService;

    public HomeController(HechoService hechoService, GeocodingService geocodingService) {
        this.hechoService = hechoService;
        this.geocodingService = geocodingService;
    }

    @GetMapping({"/", "/home"})
    public String paginaInicial(Model model) {
        TokenContext.addToken(model);

        // Obtener los 5 hechos más recientes (el backend ya los devuelve ordenados por fechaCarga)
        PageWrapper<HechoMapaOutputDto> pageWrapper = hechoService.obtenerHechos(0, 5).block();

        if (pageWrapper != null && pageWrapper.getContent() != null) {
            // Obtener direcciones para los hechos usando geocoding
            List<HechoMapaOutputDto> hechosConDireccion = pageWrapper.getContent();
            for (HechoMapaOutputDto hecho : hechosConDireccion) {
                if (hecho.getLatitud() != null && hecho.getLongitud() != null) {
                    String direccion = geocodingService.obtenerDireccionCorta(
                            hecho.getLatitud(),
                            hecho.getLongitud()
                    );
                    hecho.setDireccion(direccion);
                } else {
                    hecho.setDireccion("Sin ubicación");
                }
            }

            model.addAttribute("hechosRecientes", hechosConDireccion);
        } else {
            model.addAttribute("hechosRecientes", List.of());
        }

        return "homepage";
    }

    @GetMapping("/about")
    public String paginaAcercaDe(Model model) {
        TokenContext.addToken(model);
        return "about";
    }
}
