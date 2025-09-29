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

    @GetMapping("/provinciasConMasHechosDeColeccion")
    public ResponseEntity<List<ProvinciaConMasHechosDeColeccionDTO>> provinciasDeColeccion(@RequestParam("idColeccion") String idColeccion,@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "1") Integer limit) {
        if(validarPaginaYLimite(page, limit))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(estadisticasService.obtenerProvinciasConMasHechosDeUnaColeccion(idColeccion, page, limit));
    }

    @GetMapping("/categoriasConMasHechos")
    public ResponseEntity<List<CategoriaConMasHechosDTO>> categoriaConMasHechosReportados(@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "1") Integer limit) {
        if(validarPaginaYLimite(page, limit))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(estadisticasService.obtenerCategoriaConMasHechos(page, limit));
    }

    @GetMapping("/provinciasConMasHechosDeCategoria")
    public ResponseEntity<List<ProvinciaConMasHechosDTO>> provinciaConMasHechosDeCategoria(@RequestParam("nombreCategoria") String nombreCategoria,@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "1") Integer limit) {
        if(validarPaginaYLimite(page, limit))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(estadisticasService.obtenerProvinciaConMasHechosPorCategoria(nombreCategoria, page, limit));
    }

    @GetMapping("/horaConMasHechosDeCategoria")
    public ResponseEntity<List<HoraConMasHechosDeCategoriaDTO>> horaConMasHechosDeCategoria(@RequestParam("nombreCategoria") String nombreCategoria,@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "1") Integer limit) {
        if(validarPaginaYLimite(page, limit))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(estadisticasService.obtenerHoraConMasHechosPorCategoria(nombreCategoria, page, limit));
    }

    @GetMapping("/solicitudesDeEliminacionSpam")
    public ResponseEntity<CantidadSolicitudesSpamDTO> solicitudesSpam() {
        return ResponseEntity.ok(estadisticasService.obtenerCantidadSolicitudSpam());
    }

    public boolean validarPaginaYLimite(Integer page, Integer limit){
        return (page >= 0 && limit > 0);
    }

}
