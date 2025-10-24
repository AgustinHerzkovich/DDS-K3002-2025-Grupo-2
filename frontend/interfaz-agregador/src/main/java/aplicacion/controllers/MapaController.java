package aplicacion.controllers;

import aplicacion.dto.output.HechoMapaOutputDto;
import aplicacion.services.HechoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MapaController {

    private final HechoService hechoService;

    public MapaController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping("/mapa")
    public String paginaMapa(
            @RequestParam(name = "categoria", required = false) String categoria,
            @RequestParam(name = "fechaReporteDesde", required = false) String fechaReporteDesde,
            @RequestParam(name = "fechaReporteHasta", required = false) String fechaReporteHasta,
            @RequestParam(name = "fechaAcontecimientoDesde", required = false) String fechaAcontecimientoDesde,
            @RequestParam(name = "fechaAcontecimientoHasta", required = false) String fechaAcontecimientoHasta,
            @RequestParam(name = "latitud", required = false) Double latitud,
            @RequestParam(name = "longitud", required = false) Double longitud,
            @RequestParam(name = "search", required = false) String search,
            Model model
    ) {

        // Obtener hechos desde la API Pública
        List<HechoMapaOutputDto> hechos;

        if (categoria != null || fechaReporteDesde != null || fechaReporteHasta != null ||
            fechaAcontecimientoDesde != null || fechaAcontecimientoHasta != null ||
            latitud != null || longitud != null || search != null) {
            // Si hay filtros, usar el método con filtros
            hechos = hechoService.obtenerHechosConFiltros(
                categoria, fechaReporteDesde, fechaReporteHasta,
                fechaAcontecimientoDesde, fechaAcontecimientoHasta,
                latitud, longitud, search
            ).collectList().block(); // <--- recolecta el Flux en una lista

        } else {
            // Si no hay filtros, obtener todos los hechos
            hechos = hechoService.obtenerHechos()
                    .collectList()
                    .block(); // <--- recolecta el Flux en una lista
        }

        if (hechos == null) hechos = List.of();

        // Obtener los 5 hechos más recientes (con menor fechaCarga), sin duplicados
        List<HechoMapaOutputDto> hechosRecientes = hechos.stream()
                .filter(h -> h.getFechaCarga() != null)
                .distinct() // Elimina duplicados basándose en equals() y hashCode()
                .sorted((h1, h2) -> h2.getFechaCarga().compareTo(h1.getFechaCarga()))
                .limit(5)
                .toList();

        model.addAttribute("hechos", hechos);
        model.addAttribute("hechosRecientes", hechosRecientes);

        // Pasar los filtros actuales al modelo para mantenerlos en los inputs
        model.addAttribute("categoria", categoria != null ? categoria : "");
        model.addAttribute("fechaReporteDesde", fechaReporteDesde != null ? fechaReporteDesde : "");
        model.addAttribute("fechaReporteHasta", fechaReporteHasta != null ? fechaReporteHasta : "");
        model.addAttribute("fechaAcontecimientoDesde", fechaAcontecimientoDesde != null ? fechaAcontecimientoDesde : "");
        model.addAttribute("fechaAcontecimientoHasta", fechaAcontecimientoHasta != null ? fechaAcontecimientoHasta : "");
        model.addAttribute("search", search != null ? search : "");

        return "mapa";
    }
}