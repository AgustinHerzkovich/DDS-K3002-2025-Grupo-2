package aplicacion.controllers;

import aplicacion.dto.input.ColeccionInputDto;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.input.ModificacionAlgoritmoInputDto;
import aplicacion.dto.output.ColeccionOutputDto;
import aplicacion.excepciones.ColeccionNoEncontradaException;
import aplicacion.excepciones.FuenteNoEncontradaException;
import aplicacion.excepciones.TooHighLimitException;
import aplicacion.services.ColeccionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import aplicacion.dto.output.HechoOutputDto;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
public class ColeccionController {
    private final ColeccionService coleccionService;

    public ColeccionController(ColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    // Operaciones CREATE sobre Colecciones
    @PostMapping("/colecciones")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ColeccionOutputDto> crearColeccion(@Valid @RequestBody ColeccionInputDto coleccion) {
        ColeccionOutputDto coleccionOutput = coleccionService.guardarColeccion(coleccion);
       //coleccionService.guardarFuentesPorColeccion(coleccion, coleccion.getFuentes());
        System.out.println("Colección creada: " + coleccionOutput.getId());
        return ResponseEntity.status(201).body(coleccionOutput);
    }

    // Operaciones READ sobre Colecciones
    @GetMapping("/colecciones")
    public ResponseEntity<Page<ColeccionOutputDto>> mostrarColecciones(@RequestParam(name = "search", required = false) String textoBuscado,
                                                       @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(name = "size", defaultValue = "10") Integer size) {
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
    public ResponseEntity<?> mostrarColeccion(@PathVariable(name = "id") String idColeccion) {
        try {
            ColeccionOutputDto coleccion = coleccionService.obtenerColeccionDTO(idColeccion);
            return ResponseEntity.ok(coleccion);
        } catch (ColeccionNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/colecciones/{id}/hechosIrrestrictos")
    public ResponseEntity<?> mostrarHechosIrrestrictos(@PathVariable(name = "id") String idColeccion,
                                                          @RequestParam(name = "categoria", required = false) String categoria,
                                                          @RequestParam(name = "fechaReporteDesde", required = false) String fechaReporteDesde,
                                                          @RequestParam(name = "fechaReporteHasta", required = false) String fechaReporteHasta,
                                                          @RequestParam(name = "fechaAcontecimientoDesde", required = false) String fechaAcontecimientoDesde,
                                                          @RequestParam(name = "fechaAcontecimientoHasta", required = false) String fechaAcontecimientoHasta,
                                                          @RequestParam(name = "latitud", required = false) Double latitud,
                                                          @RequestParam(name = "longitud", required = false) Double longitud,
                                                          @RequestParam(name = "radio", required = false) Double radio,
                                                          @RequestParam(name = "search", required = false) String textoLibre,
                                                          @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                          @RequestParam(name = "size", defaultValue = "100") Integer size){

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

        try {
            Page<HechoOutputDto> hechosIrrestrictos = coleccionService.obtenerHechosIrrestrictosPorColeccion(idColeccion, categoria, fechaReporteDesdeDateTime, fechaReporteHastaDateTime, fechaAcontecimientoDesdeDateTime, fechaAcontecimientoHastaDateTime, latitud, longitud, radio, textoLibre, pageable);
            return ResponseEntity.ok(hechosIrrestrictos);
        } catch (ColeccionNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/colecciones/{id}/hechosCurados")
    public ResponseEntity<?> mostrarHechosCurados(@PathVariable(name = "id") String idColeccion,
                                                     @RequestParam(name = "categoria", required = false) String categoria,
                                                     @RequestParam(name = "fechaReporteDesde", required = false) String fechaReporteDesde,
                                                     @RequestParam(name = "fechaReporteHasta", required = false) String fechaReporteHasta,
                                                     @RequestParam(name = "fechaAcontecimientoDesde", required = false) String fechaAcontecimientoDesde,
                                                     @RequestParam(name = "fechaAcontecimientoHasta", required = false) String fechaAcontecimientoHasta,
                                                     @RequestParam(name = "latitud", required = false) Double latitud,
                                                     @RequestParam(name = "longitud", required = false) Double longitud,
                                                     @RequestParam(name = "radio", required = false) Double radio,
                                                     @RequestParam(name = "search", required = false) String textoLibre,
                                                     @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "100") Integer size){

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

        try {
            Page<HechoOutputDto> hechosCurados = coleccionService.obtenerHechosCuradosPorColeccion(idColeccion, categoria, fechaReporteDesdeDateTime, fechaReporteHastaDateTime, fechaAcontecimientoDesdeDateTime, fechaAcontecimientoHastaDateTime, latitud, longitud, radio, textoLibre, pageable);
            return ResponseEntity.ok(hechosCurados);
        } catch (ColeccionNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Operaciones UPDATE sobre Colecciones
    @PatchMapping("/colecciones/{id}/algoritmo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> modificarAlgoritmo(@PathVariable(name = "id") String idColeccion,
                                                   @Valid @RequestBody ModificacionAlgoritmoInputDto nuevoAlgoritmo) {
        try {
            ColeccionOutputDto coleccion = coleccionService.modificarAlgoritmoDeColeccion(idColeccion, nuevoAlgoritmo);
            System.out.println("Coleccion: " + idColeccion + ", nuevo algoritmo: " + nuevoAlgoritmo);
            return ResponseEntity.ok(coleccion);
        } catch (ColeccionNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/colecciones/{id}/fuentes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> agregarFuente(@PathVariable(name = "id") String idColeccion,
                                                         @Valid @RequestBody FuenteInputDto fuenteInputDto) {
        try {
            ColeccionOutputDto coleccionOutputDto = coleccionService.agregarFuenteAColeccion(idColeccion, fuenteInputDto);
            System.out.println("Coleccion: " + idColeccion + ", nueva fuente: id: " + fuenteInputDto.getId());
            return ResponseEntity.ok(coleccionOutputDto);
        }catch (ColeccionNoEncontradaException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/colecciones/{id}/fuentes/{fuenteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> quitarFuente(@PathVariable(name = "id") String idColeccion,
                                             @PathVariable(name = "fuenteId") String fuenteId) {
        try {
            ColeccionOutputDto coleccion = coleccionService.quitarFuenteDeColeccion(idColeccion, fuenteId);
            System.out.println("Coleccion: " + idColeccion + ", fuente quitada: id: " + fuenteId);
            return ResponseEntity.ok(coleccion);
        } catch (ColeccionNoEncontradaException | FuenteNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Operaciones DELETE sobre Colecciones
    @DeleteMapping("/colecciones/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarColeccion(@PathVariable(name = "id") String idColeccion) {
        try {
            coleccionService.eliminarColeccion(idColeccion);
            System.out.println("Coleccion: " + idColeccion + " eliminada");
            return ResponseEntity.noContent().build();
        } catch (ColeccionNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/colecciones/index")
    public ResponseEntity<List<String>> autoCompletar(@RequestParam(name = "search") String currentSearch, @RequestParam(name = "limit", required = false, defaultValue = "5") Integer limit){
        if(limit >100 || limit <0)
            throw new TooHighLimitException(limit);
        List<String> recomendaciones = coleccionService.obtenerAutocompletado(currentSearch, limit);
        return ResponseEntity.ok(recomendaciones);
    }
}
