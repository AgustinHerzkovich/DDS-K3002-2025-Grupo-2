package aplicacion.controllers;

import aplicacion.domain.algoritmos.TipoAlgoritmoConsenso;
import aplicacion.dto.input.ColeccionInputDto;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.output.ColeccionOutputDto;
import aplicacion.excepciones.ColeccionNoEncontradaException;
import aplicacion.excepciones.FuenteNoEncontradaException;
import aplicacion.services.ColeccionService;
import aplicacion.services.FuenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import aplicacion.dto.output.HechoOutputDto;
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
        fuenteService.guardarFuentesSiNoExisten(coleccion.getFuentes());
        ColeccionOutputDto coleccionOutput = coleccionService.guardarColeccion(coleccion);
       //coleccionService.guardarFuentesPorColeccion(coleccion, coleccion.getFuentes());
        System.out.println("Colección creada: " + coleccionOutput.getId());
        return ResponseEntity.ok(coleccionOutput);
    }

    // Operaciones READ sobre Colecciones
    @GetMapping("/colecciones")
    public List<ColeccionOutputDto> mostrarColecciones(@RequestParam(name = "search", required = false) String textoBuscado) {
        List<ColeccionOutputDto> colecciones;
        if (textoBuscado == null) {
            colecciones = coleccionService.obtenerColeccionesDTO();
        }
        else {
            colecciones = coleccionService.obtenerColeccionesPorTextoLibre(textoBuscado);
        }

        return colecciones;
    }

    @GetMapping("/colecciones/{id}")
    public ColeccionOutputDto mostrarColeccion(@PathVariable("id") String idColeccion) {
        return coleccionService.obtenerColeccionDTO(idColeccion);
    }

    @GetMapping("/colecciones/{id}/hechosIrrestrictos")
    public List<HechoOutputDto> mostrarHechosIrrestrictos(@PathVariable("id") String idColeccion,
                                                          @RequestParam(name = "categoria", required = false) String categoria,
                                                          @RequestParam(name = "fechaReporteDesde", required = false) LocalDateTime fechaReporteDesde,
                                                          @RequestParam(name = "fechaReporteHasta", required = false) LocalDateTime fechaReporteHasta,
                                                          @RequestParam(name = "fechaAcontecimientoDesde", required = false) LocalDateTime fechaAcontecimientoDesde,
                                                          @RequestParam(name = "fechaAcontecimientoHasta", required = false) LocalDateTime fechaAcontecimientoHasta,
                                                          @RequestParam(name = "latitud", required = false) Double latitud,
                                                          @RequestParam(name = "longitud", required = false) Double longitud,
                                                          @RequestParam(name = "search", required = false) String textoLibre){

        return coleccionService.obtenerHechosIrrestrictosPorColeccion(idColeccion, categoria, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud, textoLibre);
    }

    @GetMapping("/colecciones/{id}/hechosCurados")
    public List<HechoOutputDto> mostrarHechosCurados(@PathVariable("id") String idColeccion,
                                                     @RequestParam(name = "categoria", required = false) String categoria,
                                                     @RequestParam(name = "fechaReporteDesde", required = false) LocalDateTime fechaReporteDesde,
                                                     @RequestParam(name = "fechaReporteHasta", required = false) LocalDateTime fechaReporteHasta,
                                                     @RequestParam(name = "fechaAcontecimientoDesde", required = false) LocalDateTime fechaAcontecimientoDesde,
                                                     @RequestParam(name = "fechaAcontecimientoHasta", required = false) LocalDateTime fechaAcontecimientoHasta,
                                                     @RequestParam(name = "latitud", required = false) Double latitud,
                                                     @RequestParam(name = "longitud", required = false) Double longitud,
                                                     @RequestParam(name = "search", required = false) String textoLibre){

        return coleccionService.obtenerHechosCuradosPorColeccionDTO(idColeccion, categoria, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud, textoLibre);
    }

    // Operaciones UPDATE sobre Colecciones
    @PatchMapping("/colecciones/{id}/algoritmo")
    public ResponseEntity<ColeccionOutputDto> modificarAlgoritmo(@PathVariable("id") String idColeccion,
                                                   @RequestBody TipoAlgoritmoConsenso nuevoAlgoritmo) {
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
        // logica de eliminar una coleccion del repositorio
        coleccionService.eliminarColeccion(idColeccion);
        System.out.println("Coleccion: " + idColeccion + " eliminada");
        return ResponseEntity.ok().build();
    }
}
