package domain.controllers;

import java.util.List;

import domain.dto.SolicitudDTO;
import domain.excepciones.MotivoSolicitudException;
import domain.services.HechoService;
import domain.services.SolicitudService;
import domain.solicitudes.SolicitudEliminacion;
import jakarta.transaction.Transactional;
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

    @PostMapping("/solicitudes")
    public ResponseEntity<SolicitudEliminacion> crearSolicitud(@RequestBody SolicitudDTO solicitudDto) {
        try {
            SolicitudEliminacion solicitud = solicitudService.guardarSolicitudDto(solicitudDto);
            System.out.println("Solicitud creada: " + solicitud.getId() + " para el hecho: " + solicitud.getHecho().getId());
            return ResponseEntity.ok(solicitud);
        } catch (MotivoSolicitudException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/solicitudes")
    public List<SolicitudEliminacion> obtenerSolicitudes() {
        return solicitudService.obtenerSolicitudes();
    }

    @GetMapping("/solicitudes/{id}")
    public SolicitudEliminacion obtenerSolicitud(@PathVariable("id") Long id) {
        return solicitudService.obtenerSolicitud(id);
    }

    @Transactional
    @PatchMapping ("/solicitudes/{id}/estado")
    public ResponseEntity<Void> actualizarEstadoSolicitud(
            @PathVariable("id") Long id,
            @RequestBody String nuevoEstado) {
        List<SolicitudEliminacion> solis;
        SolicitudEliminacion sol;
        try {
            solis = solicitudService.solicitudesRelacionadas(id);
            sol = solicitudService.obtenerSolicitud(id); // El primero es la solicitud a cambiar
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        solicitudService.actualizarEstadoSolicitud(sol, nuevoEstado);

        for (SolicitudEliminacion solicitud : solis) {
            solicitudService.guardarSolicitud(solicitud);
        }
        hechoService.guardarHecho(sol.getHecho()); // Actualizamos el hecho (visible)

        System.out.println("Solicitud actualizada: " + sol.getId() + " a estado: " + nuevoEstado);
        return ResponseEntity.ok().build();
    }
}