package aplicacion.controllers;

import aplicacion.dto.PageWrapper;
import aplicacion.dto.TipoAlgoritmoConsenso;
import aplicacion.dto.output.HechoMapaOutputDto;
import org.springframework.ui.Model;
import aplicacion.dto.output.ColeccionOutputDto;
import aplicacion.services.ColeccionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
                                    @RequestParam(name = "page", defaultValue = "0") Integer page,
                                    @RequestParam(name = "size", defaultValue = "10") Integer size,
                                    Model model) {
        PageWrapper<ColeccionOutputDto> pageWrapper = coleccionService
                .obtenerColecciones(page, size, search)
                .block();

        if (pageWrapper == null) {
            model.addAttribute("colecciones", List.of());
            model.addAttribute("currentPage", 0);
            model.addAttribute("pageSize", size);
            model.addAttribute("hasNext", false);
            model.addAttribute("hasPrevious", false);
            model.addAttribute("totalPages", 0);

            return "colecciones";
        }

        model.addAttribute("colecciones", pageWrapper.getContent());
        model.addAttribute("search", search != null ? search : "");
        model.addAttribute("currentPage", pageWrapper.getNumber());
        model.addAttribute("pageSize", pageWrapper.getSize());
        model.addAttribute("hasNext", !pageWrapper.isLast());
        model.addAttribute("hasPrevious", !pageWrapper.isFirst());
        model.addAttribute("totalPages", pageWrapper.getTotalPages());

        return "colecciones";
    }

    @GetMapping("/colecciones/{id}/hechosIrrestrictos")
    public String paginaHechosIrrestrictosDeColeccion(@PathVariable("id") String id,
                                                      @RequestParam(name = "categoria", required = false) String categoria,
                                                      @RequestParam(name = "fechaReporteDesde", required = false) String fechaReporteDesde,
                                                      @RequestParam(name = "fechaReporteHasta", required = false) String fechaReporteHasta,
                                                      @RequestParam(name = "fechaAcontecimientoDesde", required = false) String fechaAcontecimientoDesde,
                                                      @RequestParam(name = "fechaAcontecimientoHasta", required = false) String fechaAcontecimientoHasta,
                                                      @RequestParam(name = "latitud", required = false) Double latitud,
                                                      @RequestParam(name = "longitud", required = false) Double longitud,
                                                      @RequestParam(name = "radio", required = false) Double radio,
                                                      @RequestParam(name = "search", required = false) String search,
                                                      @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                      @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                      Model model) {
        // Obtener hechos desde la API Pública
        PageWrapper<HechoMapaOutputDto> pageWrapper = coleccionService.obtenerHechosIrrestrictosDeColeccion(
                    id, categoria, fechaReporteDesde, fechaReporteHasta,
                    fechaAcontecimientoDesde, fechaAcontecimientoHasta,
                    latitud, longitud, radio, search, page, size
                    ).block();

        if (pageWrapper == null) {
            model.addAttribute("hechos", List.of());
            model.addAttribute("currentPage", 0);
            model.addAttribute("pageSize", size);
            model.addAttribute("hasNext", false);
            model.addAttribute("hasPrevious", false);
            model.addAttribute("totalPages", 0);

            return "mapa";
        }

        // Obtener los 5 hechos más recientes (con menor fechaCarga), sin duplicados
        List<HechoMapaOutputDto> hechosRecientes = pageWrapper.getContent().stream()
                .filter(h -> h.getFechaCarga() != null)
                .distinct() // Elimina duplicados basándose en equals() y hashCode()
                .sorted((h1, h2) -> h2.getFechaCarga().compareTo(h1.getFechaCarga()))
                .limit(5)
                .toList();

        model.addAttribute("hechos", pageWrapper.getContent());
        model.addAttribute("hechosRecientes", hechosRecientes);
        model.addAttribute("currentPage", pageWrapper.getNumber());
        model.addAttribute("pageSize", pageWrapper.getSize());
        model.addAttribute("hasNext", !pageWrapper.isLast());
        model.addAttribute("hasPrevious", !pageWrapper.isFirst());
        model.addAttribute("totalPages", pageWrapper.getTotalPages());

        // Pasar los filtros actuales al modelo para mantenerlos en los inputs
        model.addAttribute("categoria", categoria != null ? categoria : "");
        model.addAttribute("fechaReporteDesde", fechaReporteDesde != null ? fechaReporteDesde : "");
        model.addAttribute("fechaReporteHasta", fechaReporteHasta != null ? fechaReporteHasta : "");
        model.addAttribute("fechaAcontecimientoDesde", fechaAcontecimientoDesde != null ? fechaAcontecimientoDesde : "");
        model.addAttribute("fechaAcontecimientoHasta", fechaAcontecimientoHasta != null ? fechaAcontecimientoHasta : "");
        model.addAttribute("search", search != null ? search : "");

        return "mapa";
    }

    @GetMapping("/colecciones/{id}/hechosCurados")
    public String paginaHechosCuradosDeColeccion(@PathVariable("id") String id,
                                                 @RequestParam(name = "categoria", required = false) String categoria,
                                                 @RequestParam(name = "fechaReporteDesde", required = false) String fechaReporteDesde,
                                                 @RequestParam(name = "fechaReporteHasta", required = false) String fechaReporteHasta,
                                                 @RequestParam(name = "fechaAcontecimientoDesde", required = false) String fechaAcontecimientoDesde,
                                                 @RequestParam(name = "fechaAcontecimientoHasta", required = false) String fechaAcontecimientoHasta,
                                                 @RequestParam(name = "latitud", required = false) Double latitud,
                                                 @RequestParam(name = "longitud", required = false) Double longitud,
                                                 @RequestParam(name = "radio", required = false) Double radio,
                                                 @RequestParam(name = "search", required = false) String search,
                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                 @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                 Model model) {

        // Obtener hechos desde la API Pública
        PageWrapper<HechoMapaOutputDto> pageWrapper = coleccionService.obtenerHechosCuradosDeColeccion(
                id, categoria, fechaReporteDesde, fechaReporteHasta,
                fechaAcontecimientoDesde, fechaAcontecimientoHasta,
                latitud, longitud, radio, search, page, size
        ).block();

        if (pageWrapper == null) {
            model.addAttribute("hechos", List.of());
            model.addAttribute("currentPage", 0);
            model.addAttribute("pageSize", size);
            model.addAttribute("hasNext", false);
            model.addAttribute("hasPrevious", false);
            model.addAttribute("totalPages", 0);

            return "mapa";
        }

        // Obtener los 5 hechos más recientes (con menor fechaCarga), sin duplicados
        List<HechoMapaOutputDto> hechosRecientes = pageWrapper.getContent().stream()
                .filter(h -> h.getFechaCarga() != null)
                .distinct() // Elimina duplicados basándose en equals() y hashCode()
                .sorted((h1, h2) -> h2.getFechaCarga().compareTo(h1.getFechaCarga()))
                .limit(5)
                .toList();

        model.addAttribute("hechos", pageWrapper.getContent());
        model.addAttribute("hechosRecientes", hechosRecientes);
        model.addAttribute("currentPage", pageWrapper.getNumber());
        model.addAttribute("pageSize", pageWrapper.getSize());
        model.addAttribute("hasNext", !pageWrapper.isLast());
        model.addAttribute("hasPrevious", !pageWrapper.isFirst());
        model.addAttribute("totalPages", pageWrapper.getTotalPages());

        // Pasar los filtros actuales al modelo para mantenerlos en los inputs
        model.addAttribute("categoria", categoria != null ? categoria : "");
        model.addAttribute("fechaReporteDesde", fechaReporteDesde != null ? fechaReporteDesde : "");
        model.addAttribute("fechaReporteHasta", fechaReporteHasta != null ? fechaReporteHasta : "");
        model.addAttribute("fechaAcontecimientoDesde", fechaAcontecimientoDesde != null ? fechaAcontecimientoDesde : "");
        model.addAttribute("fechaAcontecimientoHasta", fechaAcontecimientoHasta != null ? fechaAcontecimientoHasta : "");
        model.addAttribute("search", search != null ? search : "");

        return "mapa";
    }

    @GetMapping("/colecciones/{id}")
    public String paginaColeccion(@PathVariable("id") String id, Model model) {
        ColeccionOutputDto coleccion = coleccionService.obtenerColeccion(id);
        if (coleccion == null) {
            return "error/404"; // Ver si está bien tirar esto o capaz convenga otra cosa
        }
        model.addAttribute("coleccion", coleccion);
        model.addAttribute("algoritmosDisponibles", TipoAlgoritmoConsenso.values());
        return "coleccion";
    }
}