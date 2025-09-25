package aplicacion.controllers;

import aplicacion.repositorios.agregador.HechoRepository;
import aplicacion.repositorios.olap.DimensionCategoriaRepository;
import aplicacion.services.scheduler.ActualizacionEstadisticasScheduler;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import aplicacion.domain.hechosYSolicitudes.Hecho;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {
    ActualizacionEstadisticasScheduler actualizacionEstadisticasScheduler;

    public EstadisticasController(ActualizacionEstadisticasScheduler actualizacionEstadisticasScheduler) {
        this.actualizacionEstadisticasScheduler = actualizacionEstadisticasScheduler;
    }


    @PostMapping("/actualizar")
    public ResponseEntity<Void> actualizarEstadisticas() {
        System.out.println("Actualizando estadisticas...");
        actualizacionEstadisticasScheduler.actualizarEstadisticas(); // Tarea scheduleada de inmediato
        System.out.println("Estadisticas actualizadas.");
        System.out.println("-----------------------------------");
        return ResponseEntity.noContent().build();
    }
}
