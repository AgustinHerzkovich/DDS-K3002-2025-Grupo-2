package domain.controllers;

import domain.dto.SolicitudDTO;
import domain.services.SolicitudService;
import domain.solicitudes.SolicitudEliminacion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuentesDinamicas")
public class SolicitudUsuarioController {
    private final SolicitudService solicitudService;

    public SolicitudUsuarioController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping("/solicitudes")
    public ResponseEntity<Void> crearSolicitud(@RequestBody SolicitudDTO solicitudDto) {
        solicitudService.guardarSolicitudDto(solicitudDto);
        System.out.println("Solicitud creada para el hecho: " + solicitudDto.getHechoId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/solicitudes/{id}")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable("id") Long id) {
        solicitudService.eliminarSolicitud(id);
        System.out.println("Solicitud eliminada: " + id);
        return ResponseEntity.ok().build();
    }
}
