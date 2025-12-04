package aplicacion.controllers;

import aplicacion.config.TokenContext;
import aplicacion.dto.PageWrapper;
import aplicacion.dto.output.HechoMapaOutputDto;
import aplicacion.services.GeocodingService;
import aplicacion.services.HechoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
public class MapaController {

    private final HechoService hechoService;
    private final GeocodingService geocodingService;

    public MapaController(HechoService hechoService, GeocodingService geocodingService) {
        this.hechoService = hechoService;
        this.geocodingService = geocodingService;
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
            @RequestParam(name = "radio", required = false) Double radio,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            Model model
    ) {
        TokenContext.addToken(model);

        // Obtener hechos desde la API Pública
        PageWrapper<HechoMapaOutputDto> pageWrapper;

        if (categoria != null || fechaReporteDesde != null || fechaReporteHasta != null ||
            fechaAcontecimientoDesde != null || fechaAcontecimientoHasta != null ||
            latitud != null || longitud != null || search != null) {
            // Si hay filtros, usar el metodo con filtros
            pageWrapper = hechoService.obtenerHechosConFiltros(
                categoria, fechaReporteDesde, fechaReporteHasta,
                fechaAcontecimientoDesde, fechaAcontecimientoHasta,
                latitud, longitud, radio, search, page, size
            ).block();

        } else {
            // Si no hay filtros, obtener todos los hechos
            pageWrapper = hechoService.obtenerHechos(page, size)
                    .block(); // <--- recolecta el Flux en una lista
        }

        if (pageWrapper == null || pageWrapper.getContent() == null) {
            model.addAttribute("hechos", List.of());
            model.addAttribute("currentPage", 0);
            model.addAttribute("pageSize", size);
            model.addAttribute("hasNext", false);
            model.addAttribute("hasPrevious", false);
            model.addAttribute("totalPages", 0);

            return "mapa";
        }

        List<HechoMapaOutputDto> hechos = Flux.fromIterable(pageWrapper.getContent())
                .flatMap(hecho -> {
                    if (hecho.getLatitud() != null &&
                            hecho.getLongitud() != null) {

                        return geocodingService.obtenerDireccionCorta(
                                hecho.getLatitud(),
                                hecho.getLongitud()
                        ).map(direccion -> {
                            hecho.setDireccion(direccion);
                            return hecho;
                        });
                    } else {
                        hecho.setDireccion("Sin ubicación");
                        return Mono.just(hecho);
                    }
                }, 10)
                .collectList()
                .block();

        if (hechos == null) {
            hechos = List.of();
        }

        model.addAttribute("hechos", hechos);
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
}