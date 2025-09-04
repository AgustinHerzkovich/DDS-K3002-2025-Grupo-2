package aplicacion.controllers;

import java.util.List;

import aplicacion.dto.input.SolicitudInputDTO;
import aplicacion.dto.output.SolicitudOutputDTO;
import aplicacion.excepciones.HechoNoEncontradoException;
import aplicacion.excepciones.MotivoSolicitudException;
import aplicacion.services.HechoService;
import aplicacion.services.SolicitudService;
import aplicacion.domain.solicitudes.SolicitudEliminacion;
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
    public ResponseEntity<SolicitudOutputDTO> crearSolicitud(@RequestBody SolicitudInputDTO solicitudDto) {
        try {
            SolicitudOutputDTO solicitud = solicitudService.guardarSolicitudDto(solicitudDto);
            System.out.println("Solicitud creada: " + solicitud.getId() + " para el hecho: " + solicitud.getHechoId());
            return ResponseEntity.ok(solicitud);
        } catch (MotivoSolicitudException e) {
            return ResponseEntity.badRequest().build();
        } catch (HechoNoEncontradoException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/solicitudes")
    public ResponseEntity<List<SolicitudOutputDTO>> obtenerSolicitudes() {
        return ResponseEntity.ok(solicitudService.obtenerSolicitudesDTO());
    }

    @GetMapping("/solicitudes/{id}")
    public ResponseEntity<SolicitudOutputDTO> obtenerSolicitud(@PathVariable("id") Long id) {
        return ResponseEntity.ok(solicitudService.obtenerSolicitudDTO(id));
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
            solicitudService.save(solicitud);
        }
        hechoService.guardarHecho(sol.getHecho()); // Actualizamos el hecho (visible)

        System.out.println("Solicitud actualizada: " + sol.getId() + " a estado: " + nuevoEstado);

        return ResponseEntity.ok().build();
    }
}
