package aplicacion.controllers;

import aplicacion.repositorios.agregador.HechoRepository;
import aplicacion.repositorios.olap.DimensionCategoriaRepository;
//import aplicacion.services.EstadisticasService;
import aplicacion.services.EstadisticasService;
import aplicacion.services.scheduler.ActualizacionEstadisticasScheduler;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import aplicacion.domain.hechosYSolicitudes.Hecho;
import aplicacion.dtos.CategoriaConMasHechosDTO;
import aplicacion.dtos.CantidadSolicitudesSpamDTO;
import aplicacion.dtos.HoraConMasHechosDeCategoriaDTO;
import aplicacion.dtos.ProvinciaConMasHechosDTO;
import aplicacion.dtos.ProvinciaConMasHechosDeColeccionDTO;

import java.util.List;
import java.util.Optional;
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

    @GetMapping("/provinciasDeColeccion")
    public ResponseEntity<ProvinciaConMasHechosDeColeccionDTO> provinciasDeColeccion(@RequestParam("idColeccion") String idColeccion) {
        return ResponseEntity.ok(estadisticasService.obtenerProvinciasConMasHechosDeUnaColeccion(idColeccion));
    }

    @GetMapping("/categoriaConMasDechosReportados")
    public ResponseEntity<CategoriaConMasHechosDTO> categoriaConMasHechosReportados() {
        return ResponseEntity.ok(estadisticasService.obtenerCategoriaConMasHechos());
    }

    @GetMapping("/provinciaConMasHechosPorCategoria")
    public ResponseEntity<ProvinciaConMasHechosDTO> provinciaConMasHechosDeCategoria(@RequestParam("nombreCategoria") String nombreCategoria) {
        return ResponseEntity.ok(estadisticasService.obtenerProvinciaConMasHechosPorCategoria(nombreCategoria));
    }

    @GetMapping("/horaMayorPorCategoria")
    public ResponseEntity<HoraConMasHechosDeCategoriaDTO> horaConMasHechosDeCategoria(@RequestParam("nombreCategoria") String nombreCategoria) {
        return ResponseEntity.ok(estadisticasService.obtenerHoraConMasHechosPorCategoria(nombreCategoria));
    }

    @GetMapping("/solicitudesDeEliminacionSpam")
    public ResponseEntity<CantidadSolicitudesSpamDTO> solicitudesSpam() {
        return ResponseEntity.ok(estadisticasService.obtenerCantidadSolicitudSpam());
    }
}
