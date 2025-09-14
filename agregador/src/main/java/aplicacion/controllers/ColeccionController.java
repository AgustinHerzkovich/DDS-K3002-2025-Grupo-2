package aplicacion.controllers;

import aplicacion.domain.colecciones.fuentes.FuenteId;
import aplicacion.domain.colecciones.fuentes.TipoFuente;
import aplicacion.dto.input.ColeccionInputDto;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.output.ColeccionOutputDto;
import aplicacion.dto.output.FuenteOutputDto;
import aplicacion.excepciones.ColeccionNoEncontradaException;
import aplicacion.excepciones.FuenteNoEncontradaException;
import aplicacion.services.ColeccionService;
import aplicacion.services.FuenteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import aplicacion.dto.output.HechoOutputDto;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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
        fuenteService.guardarFuentes(coleccion.getFuentes());
        ColeccionOutputDto coleccionOutput = coleccionService.guardarColeccion(coleccion);
       //coleccionService.guardarFuentesPorColeccion(coleccion, coleccion.getFuentes());
        System.out.println("Colección creada: " + coleccionOutput.getId());
        return ResponseEntity.ok(coleccionOutput);
    }

    // Operaciones READ sobre Colecciones
    @GetMapping("/colecciones")
    public List<ColeccionOutputDto> mostrarColecciones() {
        List<ColeccionOutputDto> coleccion;
        coleccion = coleccionService.obtenerColeccionesDTO();
        return coleccion;
    }

    @GetMapping("/colecciones/{id}")
    public ColeccionOutputDto mostrarColeccion(@PathVariable("id") String idColeccion) {
        return coleccionService.obtenerColeccionDTO(idColeccion);
    }

    @GetMapping("/colecciones/{id}/hechosIrrestrictos")
    public List<HechoOutputDto> mostrarHechosIrrestrictos(@PathVariable("id") String idColeccion,
                                                          @RequestParam(name = "categoria_buscada", required = false) String categoria_buscada,
                                                          @RequestParam(name = "fechaReporteDesde", required = false) LocalDateTime fechaReporteDesde,
                                                          @RequestParam(name = "fechaReporteHasta", required = false) LocalDateTime fechaReporteHasta,
                                                          @RequestParam(name = "fechaAcontecimientoDesde", required = false) LocalDateTime fechaAcontecimientoDesde,
                                                          @RequestParam(name = "fechaAcontecimientoHasta", required = false) LocalDateTime fechaAcontecimientoHasta,
                                                          @RequestParam(name = "latitud", required = false) Double latitud,
                                                          @RequestParam(name = "longitud", required = false) Double longitud) {
        return coleccionService.obtenerHechosIrrestrictosPorColeccion(idColeccion, categoria_buscada, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud);
    }

    @GetMapping("/colecciones/{id}/hechosCurados")
    public List<HechoOutputDto> mostrarHechosCurados(@PathVariable("id") String idColeccion,
                                                     @RequestParam(name = "categoria_buscada", required = false) String categoria_buscada,
                                                     @RequestParam(name = "fechaReporteDesde", required = false) LocalDateTime fechaReporteDesde,
                                                     @RequestParam(name = "fechaReporteHasta", required = false) LocalDateTime fechaReporteHasta,
                                                     @RequestParam(name = "fechaAcontecimientoDesde", required = false) LocalDateTime fechaAcontecimientoDesde,
                                                     @RequestParam(name = "fechaAcontecimientoHasta", required = false) LocalDateTime fechaAcontecimientoHasta,
                                                     @RequestParam(name = "latitud", required = false) Double latitud,
                                                     @RequestParam(name = "longitud", required = false) Double longitud){
        return coleccionService.obtenerHechosCuradosPorColeccionDTO(idColeccion, categoria_buscada, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud);
    }

    // Operaciones UPDATE sobre Colecciones
    @PatchMapping("/colecciones/{id}/algoritmo")
    public ResponseEntity<Void> modificarAlgoritmo(@PathVariable("id") String idColeccion,
                                                   @RequestBody String nuevoAlgoritmo) {
        coleccionService.modificarAlgoritmoDeColeccion(idColeccion, nuevoAlgoritmo);
        System.out.println("Coleccion: " + idColeccion + ", nuevo algoritmo: " + nuevoAlgoritmo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/colecciones/{id}/fuentes")
    public ResponseEntity<ColeccionOutputDto> agregarFuente(@PathVariable("id") String idColeccion,
                                                         @RequestBody FuenteInputDto fuenteInputDTO) {
        ColeccionOutputDto coleccionOutputDto;
        try {
            coleccionOutputDto = coleccionService.agregarFuenteAColeccion(idColeccion, fuenteInputDTO);
        }catch (ColeccionNoEncontradaException e){
            return ResponseEntity.notFound().build();
        }
        System.out.println("Coleccion: " + idColeccion + ", nueva fuente: id: " + fuenteInputDTO.getId().getIdExterno() + " tipo: " + fuenteInputDTO.getId().getTipo());
        return ResponseEntity.ok(coleccionOutputDto);
    }

    @DeleteMapping("/colecciones/{id}/fuentes/{fuenteId}/{fuenteTipo}")
    public ResponseEntity<ColeccionOutputDto> quitarFuente(@PathVariable("id") String idColeccion,
                                             @PathVariable("fuenteId") Long fuenteId,
                                             @PathVariable("fuenteTipo") TipoFuente fuenteTipo) {
        try {
            ColeccionOutputDto coleccion = coleccionService.quitarFuenteDeColeccion(idColeccion, new FuenteId(fuenteTipo, fuenteId));
            System.out.println("Coleccion: " + idColeccion + ", fuente quitada: id: " + fuenteId + " tipo: " + fuenteTipo);
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
