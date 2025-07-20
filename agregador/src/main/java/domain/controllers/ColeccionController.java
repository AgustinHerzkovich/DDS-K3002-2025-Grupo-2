package domain.controllers;

import domain.colecciones.Coleccion;
import domain.colecciones.fuentes.Fuente;
import domain.colecciones.fuentes.FuenteId;
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
    public ResponseEntity<Coleccion> crearColeccion(@RequestBody Coleccion coleccion) {
        fuenteService.guardarFuentes(coleccion.getFuentes());
        coleccionService.guardarColeccion(coleccion);
        coleccionService.guardarFuentesPorColeccion(coleccion, coleccion.getFuentes());
        System.out.println("Colección creada: " + coleccion.getIdentificadorHandle());
        return ResponseEntity.ok(coleccion);
    }

    // Operaciones READ sobre Colecciones
    @GetMapping("/colecciones")
    public List<Coleccion> mostrarColecciones() {
        return coleccionService.obtenerColecciones();
    }

    @GetMapping("/colecciones/{id}")
    public Coleccion mostrarColeccion(@PathVariable("id") String idColeccion) {
        return coleccionService.obtenerColeccion(idColeccion);
    }

    @GetMapping("/colecciones/{id}/hechosIrrestrictos")
    public List<Hecho> mostrarHechosIrrestrictos(@PathVariable("id") String idColeccion,
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
    public List<Hecho> mostrarHechosCurados(@PathVariable("id") String idColeccion,
                                            @RequestParam(name = "categoria_buscada", required = false) String categoria_buscada,
                                            @RequestParam(name = "fechaReporteDesde", required = false) LocalDateTime fechaReporteDesde,
                                            @RequestParam(name = "fechaReporteHasta", required = false) LocalDateTime fechaReporteHasta,
                                            @RequestParam(name = "fechaAcontecimientoDesde", required = false) LocalDateTime fechaAcontecimientoDesde,
                                            @RequestParam(name = "fechaAcontecimientoHasta", required = false) LocalDateTime fechaAcontecimientoHasta,
                                            @RequestParam(name = "latitud", required = false) Double latitud,
                                            @RequestParam(name = "longitud", required = false) Double longitud){
        return coleccionService.obtenerHechosCuradosPorColeccion(idColeccion, categoria_buscada, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud);
    }

    @PatchMapping("/colecciones/{id}/algoritmo")
    public ResponseEntity<Void> modificarAlgoritmo(@PathVariable("id") String idColeccion,
                                                   @RequestBody String nuevoAlgoritmo) {
        coleccionService.modificarAlgoritmoDeColeccion(idColeccion, nuevoAlgoritmo);
        System.out.println("Coleccion: " + idColeccion + ", nuevo algoritmo: " + nuevoAlgoritmo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/colecciones/{id}/fuentes")
    public ResponseEntity<Void> agregarFuente(@PathVariable("id") String idColeccion,
                                              @RequestBody Fuente fuente) {
        fuenteService.guardarFuente(fuente);
        Coleccion coleccion = coleccionService.obtenerColeccion(idColeccion);
        coleccionService.agregarFuenteAColeccion(coleccion, fuente);
        coleccionService.guardarColeccion(coleccion);
        System.out.println("Coleccion: " + idColeccion + ", nueva fuente: id: " + fuente.getId().getIdExterno() + " tipo: " + fuente.getId().getTipo());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/colecciones/{id}/fuentes")
    public ResponseEntity<Void> quitarFuente(@PathVariable("id") String idColeccion,
                                             @RequestBody FuenteId fuenteId) {
        Coleccion coleccion = coleccionService.obtenerColeccion(idColeccion);
        coleccionService.quitarFuenteDeColeccion(coleccion, fuenteId); // TODO: Hacer que se updatee bien la colección
        System.out.println("Coleccion: " + idColeccion + ", fuente quitada: id: " + fuenteId.getIdExterno() + " tipo: " + fuenteId.getTipo());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/colecciones/{id}")
    public ResponseEntity<Void> eliminarColeccion(@PathVariable("id") String idColeccion) {
        // logica de eliminar una coleccion del repositorio
        coleccionService.eliminarColeccion(idColeccion);
        System.out.println("Coleccion: " + idColeccion + " eliminada");
        return ResponseEntity.ok().build();
    }
}