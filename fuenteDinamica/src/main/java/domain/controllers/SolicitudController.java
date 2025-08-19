package domain.controllers;

import domain.dto.SolicitudDTO;
import domain.services.SolicitudService;
import domain.solicitudes.SolicitudEliminacion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuentesDinamicas")
public class SolicitudController {
    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping("/solicitudes")
    public List<SolicitudEliminacion> obtenerSolicitudes() {
        return solicitudService.obtenerSolicitudes();
    }

    @GetMapping("/solicitudes/{id}")
    public SolicitudEliminacion obtenerSolicitud(@PathVariable("id") Long id) {
        return solicitudService.obtenerSolicitud(id);
    }
}
