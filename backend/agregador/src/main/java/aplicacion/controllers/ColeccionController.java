package aplicacion.controllers;

import aplicacion.domain.algoritmos.TipoAlgoritmoConsenso;
import aplicacion.dto.input.ColeccionInputDto;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.input.ModificacionAlgoritmoInputDto;
import aplicacion.dto.output.ColeccionOutputDto;
import aplicacion.excepciones.ColeccionNoEncontradaException;
import aplicacion.excepciones.FuenteNoEncontradaException;
import aplicacion.services.ColeccionService;
import aplicacion.services.FuenteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import aplicacion.dto.output.HechoOutputDto;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/agregador")
public class ColeccionController {
    private final ColeccionService coleccionService;
    private final FuenteService fuenteService;
    public ColeccionController(ColeccionService coleccionService, FuenteService fuenteService) {
        this.coleccionService = coleccionService;
        this.fuenteService = fuenteService;
    }

    // Operaciones CREATE sobre Colecciones
    @PostMapping("/colecciones")
    public ResponseEntity<ColeccionOutputDto> crearColeccion(@RequestBody ColeccionInputDto coleccion) {
        ColeccionOutputDto coleccionOutput = coleccionService.guardarColeccion(coleccion);
       //coleccionService.guardarFuentesPorColeccion(coleccion, coleccion.getFuentes());
        System.out.println("Colección creada: " + coleccionOutput.getId());
        return ResponseEntity.ok(coleccionOutput);
    }

    // Operaciones READ sobre Colecciones
    @GetMapping("/colecciones")
    public ResponseEntity<Page<ColeccionOutputDto>> mostrarColecciones(@RequestParam(name = "search", required = false) String textoBuscado,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ColeccionOutputDto> colecciones;

        if (textoBuscado == null) {
            colecciones = coleccionService.obtenerColeccionesDTO(pageable);
        }
        else {
            colecciones = coleccionService.obtenerColeccionesPorTextoLibre(textoBuscado, pageable);
        }

        return ResponseEntity.ok(colecciones);
    }

    @GetMapping("/colecciones/{id}")
    public ColeccionOutputDto mostrarColeccion(@PathVariable("id") String idColeccion) {
        return coleccionService.obtenerColeccionDTO(idColeccion);
    }

    @GetMapping("/colecciones/{id}/hechosIrrestrictos")
    public ResponseEntity<Page<HechoOutputDto>> mostrarHechosIrrestrictos(@PathVariable("id") String idColeccion,
                                                          @RequestParam(name = "categoria", required = false) String categoria,
                                                          @RequestParam(name = "fechaReporteDesde", required = false) String fechaReporteDesde,
                                                          @RequestParam(name = "fechaReporteHasta", required = false) String fechaReporteHasta,
                                                          @RequestParam(name = "fechaAcontecimientoDesde", required = false) String fechaAcontecimientoDesde,
                                                          @RequestParam(name = "fechaAcontecimientoHasta", required = false) String fechaAcontecimientoHasta,
                                                          @RequestParam(name = "latitud", required = false) Double latitud,
                                                          @RequestParam(name = "longitud", required = false) Double longitud,
                                                          @RequestParam(name = "search", required = false) String textoLibre,
                                                          @RequestParam(defaultValue = "0") Integer page,
                                                          @RequestParam(defaultValue = "100") Integer size){

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

        Page<HechoOutputDto> hechosIrrestrictos = coleccionService.obtenerHechosIrrestrictosPorColeccion(idColeccion, categoria, fechaReporteDesdeDateTime, fechaReporteHastaDateTime, fechaAcontecimientoDesdeDateTime, fechaAcontecimientoHastaDateTime, latitud, longitud, textoLibre, pageable);
        return ResponseEntity.ok(hechosIrrestrictos);
    }

    @GetMapping("/colecciones/{id}/hechosCurados")
    public ResponseEntity<Page<HechoOutputDto>> mostrarHechosCurados(@PathVariable("id") String idColeccion,
                                                     @RequestParam(name = "categoria", required = false) String categoria,
                                                     @RequestParam(name = "fechaReporteDesde", required = false) String fechaReporteDesde,
                                                     @RequestParam(name = "fechaReporteHasta", required = false) String fechaReporteHasta,
                                                     @RequestParam(name = "fechaAcontecimientoDesde", required = false) String fechaAcontecimientoDesde,
                                                     @RequestParam(name = "fechaAcontecimientoHasta", required = false) String fechaAcontecimientoHasta,
                                                     @RequestParam(name = "latitud", required = false) Double latitud,
                                                     @RequestParam(name = "longitud", required = false) Double longitud,
                                                     @RequestParam(name = "search", required = false) String textoLibre,
                                                     @RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "100") Integer size){

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

        Page<HechoOutputDto> hechosCurados = coleccionService.obtenerHechosCuradosPorColeccionDTO(idColeccion, categoria, fechaReporteDesdeDateTime, fechaReporteHastaDateTime, fechaAcontecimientoDesdeDateTime, fechaAcontecimientoHastaDateTime, latitud, longitud, textoLibre, pageable);
        return ResponseEntity.ok(hechosCurados);
    }

    // Operaciones UPDATE sobre Colecciones
    @PatchMapping("/colecciones/{id}/algoritmo")
    public ResponseEntity<ColeccionOutputDto> modificarAlgoritmo(@PathVariable("id") String idColeccion,
                                                   @RequestBody ModificacionAlgoritmoInputDto nuevoAlgoritmo) {
        ColeccionOutputDto coleccion = coleccionService.modificarAlgoritmoDeColeccion(idColeccion, nuevoAlgoritmo);
        System.out.println("Coleccion: " + idColeccion + ", nuevo algoritmo: " + nuevoAlgoritmo);
        return ResponseEntity.ok(coleccion);
    }

    @PostMapping("/colecciones/{id}/fuentes")
    public ResponseEntity<ColeccionOutputDto> agregarFuente(@PathVariable("id") String idColeccion,
                                                         @RequestBody FuenteInputDto fuenteInputDto) {
        ColeccionOutputDto coleccionOutputDto;
        try {
            coleccionOutputDto = coleccionService.agregarFuenteAColeccion(idColeccion, fuenteInputDto);
        }catch (ColeccionNoEncontradaException e){
            return ResponseEntity.notFound().build();
        }
        System.out.println("Coleccion: " + idColeccion + ", nueva fuente: id: " + fuenteInputDto.getId());
        return ResponseEntity.ok(coleccionOutputDto);
    }

    @DeleteMapping("/colecciones/{id}/fuentes/{fuenteId}")
    public ResponseEntity<ColeccionOutputDto> quitarFuente(@PathVariable("id") String idColeccion,
                                             @PathVariable("fuenteId") String fuenteId) {
        try {
            ColeccionOutputDto coleccion = coleccionService.quitarFuenteDeColeccion(idColeccion, fuenteId);
            System.out.println("Coleccion: " + idColeccion + ", fuente quitada: id: " + fuenteId);
            return ResponseEntity.ok(coleccion);
        } catch (ColeccionNoEncontradaException | FuenteNoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Operaciones DELETE sobre Colecciones
    @DeleteMapping("/colecciones/{id}")
    public ResponseEntity<Void> eliminarColeccion(@PathVariable("id") String idColeccion) {
        try {
            coleccionService.eliminarColeccion(idColeccion);
            System.out.println("Coleccion: " + idColeccion + " eliminada");
            return ResponseEntity.ok().build();
        } catch (ColeccionNoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
