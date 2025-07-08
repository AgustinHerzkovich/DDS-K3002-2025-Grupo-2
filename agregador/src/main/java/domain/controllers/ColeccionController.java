package domain.controllers;

import domain.colecciones.Coleccion;
import domain.hechos.Hecho;
import domain.services.ColeccionService;
import domain.services.FuenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Coleccion> crearColeccion(Coleccion coleccion) {
        coleccionService.guardarColeccion(coleccion);
        fuenteService.guardarFuentes(coleccion.getFuentes());
        coleccionService.guardarFuentesPorColeccion(coleccion.getIdentificadorHandle(), coleccion.getFuentes()); // todo: seguir desde aca
        return ResponseEntity.ok(coleccion);
    }

    // Operaciones READ sobre Colecciones
    @GetMapping("/colecciones")
    public List<Coleccion> mostrarColecciones() {
        return coleccionService.obtenerColecciones();
    }

    @GetMapping("/colecciones/{id}/hechosIrrestrictos")
    public List<Hecho> mostrarHechosIrrestrictos(@PathVariable("id") String idColeccion,
                                                 @RequestParam(required=false) String categoria_buscada,
                                                 @RequestParam(required=false) LocalDateTime fechaReporteDesde,
                                                 @RequestParam(required=false) LocalDateTime fechaReporteHasta,
                                                 @RequestParam(required=false) LocalDateTime fechaAcontecimientoDesde,
                                                 @RequestParam(required=false) LocalDateTime fechaAcontecimientoHasta,
                                                 @RequestParam(required=false) Double latitud,
                                                 @RequestParam(required=false) Double longitud) {
        return coleccionService.obtenerHechosIrrestrictosPorColeccion(idColeccion, categoria_buscada, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud);
    }

    @GetMapping("/colecciones/{id}/hechosCurados")
    public List<Hecho> mostrarHechosCurados(@PathVariable("id") String idColeccion,
                                            @RequestParam(required=false) String categoria_buscada,
                                            @RequestParam(required=false) LocalDateTime fechaReporteDesde,
                                            @RequestParam(required=false) LocalDateTime fechaReporteHasta,
                                            @RequestParam(required=false) LocalDateTime fechaAcontecimientoDesde,
                                            @RequestParam(required=false) LocalDateTime fechaAcontecimientoHasta,
                                            @RequestParam(required=false) Double latitud,
                                            @RequestParam(required=false) Double longitud){
        return coleccionService.obtenerHechosCuradosPorColeccion(idColeccion, categoria_buscada, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud);
    }

    @DeleteMapping("/colecciones/{id}")
    public ResponseEntity<Void> eliminarColeccion(@PathVariable("id") String idColeccion) {
        // logica de eliminar una coleccion del repositorio
        coleccionService.eliminarColeccion(idColeccion);
        return ResponseEntity.noContent().build();
    }
}

// Operaciones UPDATE sobre Colecciones
//PATCH MAPPING
// Operaciones DELETE sobre Colecciones
//DELETE MAPPING

// modificacion del algoritmo: PATCH sobre coleccion
// Agregar o quitar fuentes de hechos de una colección.: PATCH sobre coleccion
// Aprobar o denegar una solicitud de eliminación de un hecho.: PATCH sobre solicitud

// Generar una solicitud de eliminación a un hecho.: POST sobre solicitud
// Reportar un hecho: POST sobre reporte ???
