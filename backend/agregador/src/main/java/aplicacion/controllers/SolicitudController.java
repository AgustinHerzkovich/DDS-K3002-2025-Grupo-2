package aplicacion.controllers;

import java.util.List;

import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.dto.input.RevisionSolicitudInputDto;
import aplicacion.dto.input.SolicitudInputDto;
import aplicacion.dto.output.SolicitudOutputDto;
import aplicacion.excepciones.HechoNoEncontradoException;
import aplicacion.excepciones.MotivoSolicitudException;
import aplicacion.services.ContribuyenteService;
import aplicacion.services.HechoService;
import aplicacion.services.SolicitudService;
import aplicacion.domain.solicitudes.SolicitudEliminacion;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agregador")
public class SolicitudController {
    private final SolicitudService solicitudService;
    private final HechoService hechoService;
    private final ContribuyenteService contribuyenteService;

    public SolicitudController(SolicitudService solicitudService, HechoService hechoService, ContribuyenteService contribuyenteService) {
        this.solicitudService = solicitudService;
        this.hechoService = hechoService;
        this.contribuyenteService = contribuyenteService;
    }

    @PostMapping("/solicitudes")
    public ResponseEntity<SolicitudOutputDto> crearSolicitud(@RequestBody SolicitudInputDto solicitudDto) {
        try {
            SolicitudOutputDto solicitud = solicitudService.guardarSolicitudDto(solicitudDto);
            System.out.println("Solicitud creada: " + solicitud.getId() + " para el hecho: " + solicitud.getHechoId());
            return ResponseEntity.ok(solicitud);
        } catch (MotivoSolicitudException e) {
            return ResponseEntity.badRequest().build();
        } catch (HechoNoEncontradoException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/solicitudes")
    public ResponseEntity<Page<SolicitudOutputDto>> obtenerSolicitudes(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                       @RequestParam(name = "size", defaultValue = "3") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(solicitudService.obtenerSolicitudesDTO(pageable));
    }

    @GetMapping("/solicitudes/{id}")
    public ResponseEntity<SolicitudOutputDto> obtenerSolicitud(@PathVariable("id") Long id) {
        return ResponseEntity.ok(solicitudService.obtenerSolicitudDTO(id));
    }

    @Transactional
    @PatchMapping ("/solicitudes/{id}/estado")
    public ResponseEntity<Void> actualizarEstadoSolicitud(
            @PathVariable("id") Long id,
            @RequestBody RevisionSolicitudInputDto revisionSolicitudInputDto){
        List<SolicitudEliminacion> solis;
        SolicitudEliminacion sol;
        Contribuyente contribuyente;
        String nuevoEstado = revisionSolicitudInputDto.getNuevoEstado();
        Long adminId = revisionSolicitudInputDto.getAdminId();
        try {
            solis = solicitudService.solicitudesRelacionadas(id);
            sol = solicitudService.obtenerSolicitud(id); // El primero es la solicitud a cambiar
            contribuyente = contribuyenteService.obtenerContribuyentePorId(adminId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        solicitudService.actualizarEstadoSolicitud(sol, nuevoEstado, contribuyente);

        for (SolicitudEliminacion solicitud : solis) {
            solicitudService.save(solicitud);
        }
        hechoService.guardarHecho(sol.getHecho()); // Actualizamos el hecho (visible)

        System.out.println("Solicitud actualizada: " + sol.getId() + " a estado: " + nuevoEstado);

        return ResponseEntity.ok().build();
    }
}
