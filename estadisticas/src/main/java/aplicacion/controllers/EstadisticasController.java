package aplicacion.controllers;

import aplicacion.domain.dimensiones.DimensionCategoria;
import aplicacion.domain.dimensiones.DimensionTiempo;
import aplicacion.domain.dimensiones.DimensionUbicacion;
import aplicacion.domain.facts.FactHecho;
import aplicacion.domain.id.FactHechoId;
import aplicacion.repositorios.agregador.HechoRepository;
import aplicacion.repositorios.olap.DimensionCategoriaRepository;
import aplicacion.repositorios.olap.FactHechoRepository;
import aplicacion.services.scheduler.ActualizacionEstadisticasScheduler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import aplicacion.domain.hechosYSolicitudes.Hecho;

import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {
    ActualizacionEstadisticasScheduler actualizacionEstadisticasScheduler;
    public EstadisticasController(ActualizacionEstadisticasScheduler actualizacionEstadisticasScheduler, FactHechoRepository factHechoRepository) {
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
