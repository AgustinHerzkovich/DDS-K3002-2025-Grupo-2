package aplicacion.controllers;

import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.excepciones.HechoNoEncontradoException;
import aplicacion.services.HechoService;
import aplicacion.services.schedulers.CargarHechosScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/agregador")
public class HechoController {
    private final HechoService hechoService;
    private final CargarHechosScheduler cargarHechosScheduler;

    public HechoController(HechoService hechoService, CargarHechosScheduler cargarHechosScheduler) {
        this.hechoService = hechoService;
        this.cargarHechosScheduler = cargarHechosScheduler;
    }

    @GetMapping("/hechos")
    public List<HechoOutputDto> obtenerHechos(@RequestParam(name = "categoria", required = false) String categoria,
                                               @RequestParam(name = "fechaReporteDesde", required = false) String fechaReporteDesde,
                                               @RequestParam(name = "fechaReporteHasta", required = false) String fechaReporteHasta,
                                               @RequestParam(name = "fechaAcontecimientoDesde", required = false) String fechaAcontecimientoDesde,
                                               @RequestParam(name = "fechaAcontecimientoHasta", required = false) String fechaAcontecimientoHasta,
                                               @RequestParam(name = "latitud", required = false) Double latitud,
                                               @RequestParam(name = "longitud", required = false) Double longitud,
                                               @RequestParam(name = "search", required = false) String textoBuscado) {

        // Decodificar y convertir strings de fecha a LocalDateTime
        LocalDateTime fechaReporteDesdeDateTime = fechaReporteDesde != null ?
            LocalDateTime.parse(URLDecoder.decode(fechaReporteDesde, StandardCharsets.UTF_8)) : null;
        LocalDateTime fechaReporteHastaDateTime = fechaReporteHasta != null ?
            LocalDateTime.parse(URLDecoder.decode(fechaReporteHasta, StandardCharsets.UTF_8)) : null;
        LocalDateTime fechaAcontecimientoDesdeDateTime = fechaAcontecimientoDesde != null ?
            LocalDateTime.parse(URLDecoder.decode(fechaAcontecimientoDesde, StandardCharsets.UTF_8)) : null;
        LocalDateTime fechaAcontecimientoHastaDateTime = fechaAcontecimientoHasta != null ?
            LocalDateTime.parse(URLDecoder.decode(fechaAcontecimientoHasta, StandardCharsets.UTF_8)) : null;

        List<HechoOutputDto> hechos;
        if (textoBuscado == null) {
            hechos = hechoService.obtenerHechosAsDTO(categoria, fechaReporteDesdeDateTime, fechaReporteHastaDateTime, fechaAcontecimientoDesdeDateTime, fechaAcontecimientoHastaDateTime, latitud, longitud);
        }
        else
        {
            hechos = hechoService.obtenerHechosPorTextoLibreDto(categoria, fechaReporteDesdeDateTime, fechaReporteHastaDateTime, fechaAcontecimientoDesdeDateTime, fechaAcontecimientoHastaDateTime, latitud, longitud, textoBuscado);
        }
        return hechos;
    }

    @GetMapping("/hechos/{id}")
    public ResponseEntity<HechoOutputDto> obtenerHechoPorId(@PathVariable("id") String id) {
        try {
            HechoOutputDto hecho = hechoService.obtenerHechoDto(id);
            return ResponseEntity.ok(hecho);
        } catch (HechoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/hechos")
    public ResponseEntity<HechoOutputDto> reportarHecho(@RequestBody HechoInputDto hechoInputDto) {
        HechoOutputDto hecho = hechoService.agregarHecho(hechoInputDto);
        System.out.println("Hecho creado: " + hecho.getId());
        return ResponseEntity.ok(hecho);
    }

    @PostMapping("/cargarHechos")
    public ResponseEntity<Void> cargarHechos() { // Endpoint para disparar la carga de hechos manualmente (si es necesario)
        cargarHechosScheduler.cargarHechos();
        return ResponseEntity.ok().build();
    }
}
