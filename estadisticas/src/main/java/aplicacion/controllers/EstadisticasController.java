package aplicacion.controllers;

import aplicacion.dtos.*;
//import aplicacion.services.EstadisticasService;
import aplicacion.services.EstadisticasService;
import aplicacion.services.scheduler.ActualizacionEstadisticasScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {
    ActualizacionEstadisticasScheduler actualizacionEstadisticasScheduler;
    EstadisticasService estadisticasService;

    public EstadisticasController(ActualizacionEstadisticasScheduler actualizacionEstadisticasScheduler, EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
        this.actualizacionEstadisticasScheduler = actualizacionEstadisticasScheduler;
    }


    @PostMapping("/actualizar")
    public ResponseEntity<Void> actualizarEstadisticas() {
        System.out.println("Actualizando estadisticas...");
        actualizacionEstadisticasScheduler.actualizarEstadisticas(); // Tarea scheduleada de inmediato
        System.out.println("""
                ---------------------------------
                    Estadisticas actualizadas.
                ---------------------------------
                """);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/provinciasConMasHechosDeColeccion")
    public ResponseEntity<List<ProvinciaConMasHechosDeColeccionDTO>> provinciasDeColeccion(@RequestParam("idColeccion") String idColeccion,@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "1") Integer limit) {
        if(isPaginaYLimiteInvalid(page, limit))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(estadisticasService.obtenerProvinciasConMasHechosDeUnaColeccion(idColeccion, page, limit));
    }

    @GetMapping("/categoriasConMasHechos")
    public ResponseEntity<List<CategoriaConMasHechosDTO>> categoriaConMasHechosReportados(@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "1") Integer limit) {
        if(isPaginaYLimiteInvalid(page, limit))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(estadisticasService.obtenerCategoriaConMasHechos(page, limit));
    }

    @GetMapping("/provinciasConMasHechosDeCategoria")
    public ResponseEntity<List<ProvinciaConMasHechosDTO>> provinciaConMasHechosDeCategoria(@RequestParam("nombreCategoria") String nombreCategoria,@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "1") Integer limit) {
        if(isPaginaYLimiteInvalid(page, limit))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(estadisticasService.obtenerProvinciaConMasHechosPorCategoria(nombreCategoria, page, limit));
    }

    @GetMapping("/horaConMasHechosDeCategoria")
    public ResponseEntity<List<HoraConMasHechosDeCategoriaDTO>> horaConMasHechosDeCategoria(@RequestParam("nombreCategoria") String nombreCategoria,@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "1") Integer limit) {
        if(isPaginaYLimiteInvalid(page, limit))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(estadisticasService.obtenerHoraConMasHechosPorCategoria(nombreCategoria, page, limit));
    }

    @GetMapping("/solicitudesDeEliminacionSpam")
    public ResponseEntity<CantidadSolicitudesSpamDTO> solicitudesSpam() {
        return ResponseEntity.ok(estadisticasService.obtenerCantidadSolicitudSpam());
    }

    @GetMapping("/coleccionesDisponibles")
    public ResponseEntity<List<ColeccionDisponibleDTO>> coleccionesDisponibles(@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        if(isPaginaYLimiteInvalid(page, limit))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(estadisticasService.obtenerColeccionesDisponibles(page, limit));
    }

    public boolean isPaginaYLimiteInvalid(Integer page, Integer limit){
        return !(page >= 0 && limit > 0);
    }

}
