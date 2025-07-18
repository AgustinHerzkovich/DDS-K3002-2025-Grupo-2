package domain.controllers;

import java.util.List;

import domain.dto.SolicitudDTO;
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
    public ResponseEntity<SolicitudEliminacion> crearSolicitud(@RequestBody SolicitudDTO solicitudDto) {
        SolicitudEliminacion solicitud = solicitudService.guardarSolicitudDto(solicitudDto);
        System.out.println("Solicitud creada: " + solicitud.getId() + " para el hecho: " + solicitud.getHecho().getId());
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
            sol = solicitudService.obtenerSolicitud(id); // El primero es la solicitud a cambiar
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        solicitudService.actualizarEstadoSolicitud(sol, nuevoEstado);

        for (SolicitudEliminacion solicitud : solis) {
            solicitudService.guardarSolicitud(solicitud); // TODO: se deben actualizar tambien estas
        }
        hechoService.guardarHecho(sol.getHecho()); // Actualizamos el hecho (visible) TODO: se debe actualizar el hecho antes de volver a guardarlo, es decir quitarle las solicitudes

        System.out.println("Solicitud actualizada: " + sol.getId() + " a estado: " + nuevoEstado);
        return ResponseEntity.ok().build();
    }
}