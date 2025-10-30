package aplicacion.controllers;

import aplicacion.domain.hechos.Etiqueta;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.mappers.EtiquetaOutputMapper;
import aplicacion.dto.output.EtiquetaOutputDTO;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.excepciones.EtiquetaNoEncontradaException;
import aplicacion.excepciones.HechoNoEncontradoException;
import aplicacion.services.HechoService;
import aplicacion.services.schedulers.CargarHechosScheduler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<HechoOutputDto>> obtenerHechos(@RequestParam(name = "categoria", required = false) String categoria,
                                               @RequestParam(name = "fechaReporteDesde", required = false) String fechaReporteDesde,
                                               @RequestParam(name = "fechaReporteHasta", required = false) String fechaReporteHasta,
                                               @RequestParam(name = "fechaAcontecimientoDesde", required = false) String fechaAcontecimientoDesde,
                                               @RequestParam(name = "fechaAcontecimientoHasta", required = false) String fechaAcontecimientoHasta,
                                               @RequestParam(name = "latitud", required = false) Double latitud,
                                               @RequestParam(name = "longitud", required = false) Double longitud,
                                               @RequestParam(name = "search", required = false) String textoBuscado,
                                              @RequestParam(defaultValue = "0") Integer page,
                                              @RequestParam(defaultValue = "100") Integer size) {

        // Decodificar y convertir strings de fecha a LocalDateTime
        LocalDateTime fechaReporteDesdeDateTime = fechaReporteDesde != null ?
            LocalDateTime.parse(URLDecoder.decode(fechaReporteDesde, StandardCharsets.UTF_8)) : null;
        LocalDateTime fechaReporteHastaDateTime = fechaReporteHasta != null ?
            LocalDateTime.parse(URLDecoder.decode(fechaReporteHasta, StandardCharsets.UTF_8)) : null;
        LocalDateTime fechaAcontecimientoDesdeDateTime = fechaAcontecimientoDesde != null ?
            LocalDateTime.parse(URLDecoder.decode(fechaAcontecimientoDesde, StandardCharsets.UTF_8)) : null;
        LocalDateTime fechaAcontecimientoHastaDateTime = fechaAcontecimientoHasta != null ?
            LocalDateTime.parse(URLDecoder.decode(fechaAcontecimientoHasta, StandardCharsets.UTF_8)) : null;

        Pageable pageable = PageRequest.of(page, size);

        Page<HechoOutputDto> hechos;
        if (textoBuscado == null) {
            hechos = hechoService.obtenerHechosAsDTO(categoria, fechaReporteDesdeDateTime, fechaReporteHastaDateTime, fechaAcontecimientoDesdeDateTime, fechaAcontecimientoHastaDateTime, latitud, longitud, pageable);
        }
        else
        {
            hechos = hechoService.obtenerHechosPorTextoLibreDto(categoria, fechaReporteDesdeDateTime, fechaReporteHastaDateTime, fechaAcontecimientoDesdeDateTime, fechaAcontecimientoHastaDateTime, latitud, longitud, textoBuscado, pageable);
        }
        return ResponseEntity.ok(hechos);
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
    @PostMapping("/hechos/{id}/tags")
    public ResponseEntity<EtiquetaOutputDTO> agregarEtiqueta(@PathVariable(name = "id") String hechoId, @RequestBody String etiquetaName) throws HechoNoEncontradoException {
        Etiqueta etiqueta = hechoService.agregarEtiqueta(hechoId, etiquetaName);
        System.out.println("Se agrego el tag: " + etiquetaName);
        return ResponseEntity.ok(EtiquetaOutputMapper.map(etiqueta));
    }
    @DeleteMapping("/hechos/{hechoId}/tags/{tag}")
    public ResponseEntity<Void> eliminarEtiqueta(@PathVariable(name = "hechoId") String hechoId, @PathVariable(name = "tag") String etiquetaName) throws HechoNoEncontradoException, EtiquetaNoEncontradaException {
        HechoOutputDto hecho = hechoService.eliminarEtiqueta(hechoId, etiquetaName);
        System.out.println("Se elimino el tag: " + etiquetaName);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/cargarHechos")
    public ResponseEntity<Void> cargarHechos() { // Endpoint para disparar la carga de hechos manualmente (si es necesario)
        cargarHechosScheduler.cargarHechos();
        return ResponseEntity.ok().build();
    }
}
