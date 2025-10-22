package aplicacion.controllers;

import aplicacion.dto.output.HechoOutputDto;
import aplicacion.services.HechoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
        List<HechoOutputDto> hechos;

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

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            String hechosJson = mapper.writeValueAsString(hechos);
            model.addAttribute("hechosJson", hechosJson);
        } catch (Exception e) {
            System.err.println("Error al convertir hechos a JSON: " + e.getMessage());
        }

        return "mapa";
    }
}