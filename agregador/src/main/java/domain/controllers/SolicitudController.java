package domain.controllers;

import java.util.List;
import domain.services.HechoService;
import domain.services.SolicitudService;
import domain.solicitudes.SolicitudEliminacion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agregador")
public class SolicitudController {
    private final SolicitudService solicitudService;
    private final HechoService hechoService;
    public SolicitudController(SolicitudService solicitudService, HechoService hechoService) {
        this.solicitudService = solicitudService;
        this.hechoService = hechoService;
    }

    @PostMapping("/solicitudes/")
    public ResponseEntity<SolicitudEliminacion> crearSolicitud(SolicitudEliminacion solicitud) {
        solicitudService.guardarSolicitud(solicitud);
        return ResponseEntity.ok(solicitud);
    }

    @PatchMapping ("/solicitudes/{id}/estado")
    public ResponseEntity<Void> actualizarEstadoSolicitud(
            @PathVariable("id") Long id,
            @RequestBody String nuevoEstado) {
        List<SolicitudEliminacion> solis;
        SolicitudEliminacion sol;
        try {
            solis = solicitudService.solicitudesRelacionadas(id);
            sol = solis.getFirst(); // El primero es la solicitud a cambiar
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        solicitudService.actualizarEstadoSolicitud(sol, nuevoEstado);

        for (SolicitudEliminacion solicitud : solis) {
            solicitudService.guardarSolicitud(solicitud);
        }
        hechoService.guardarHecho(sol.getHecho()); // Actualizamos el hecho (visible)

        return ResponseEntity.ok().build();
    }
}