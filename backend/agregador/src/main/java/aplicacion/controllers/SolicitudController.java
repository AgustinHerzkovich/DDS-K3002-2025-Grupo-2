package aplicacion.controllers;

import java.util.List;

import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.dto.input.RevisionSolicitudInputDto;
import aplicacion.dto.input.SolicitudInputDto;
import aplicacion.dto.output.SolicitudOutputDto;
import aplicacion.excepciones.ContribuyenteNoConfiguradoException;
import aplicacion.excepciones.HechoNoEncontradoException;
import aplicacion.excepciones.MotivoSolicitudException;
import aplicacion.services.ContribuyenteService;
import aplicacion.services.HechoService;
import aplicacion.services.SolicitudService;
import aplicacion.domain.solicitudes.SolicitudEliminacion;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
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
    public ResponseEntity<?> crearSolicitud(@Valid @RequestBody SolicitudInputDto solicitudDto) {
        try {
            SolicitudOutputDto solicitud = solicitudService.guardarSolicitudDto(solicitudDto);
            System.out.println("Solicitud creada: " + solicitud.getId() + " para el hecho: " + solicitud.getHechoId());
            return ResponseEntity.status(201).body(solicitud);
        } catch (MotivoSolicitudException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (HechoNoEncontradoException | ContribuyenteNoConfiguradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/solicitudes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<SolicitudOutputDto>> obtenerSolicitudes(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                       @RequestParam(name = "size", defaultValue = "3") Integer size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authorities: " + auth.getAuthorities());
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(solicitudService.obtenerSolicitudesDTO(pageable));
    }

    @GetMapping("/solicitudes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SolicitudOutputDto> obtenerSolicitud(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(solicitudService.obtenerSolicitudDTO(id));
    }

    @Transactional
    @PatchMapping ("/solicitudes/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarEstadoSolicitud(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody RevisionSolicitudInputDto revisionSolicitudInputDto){
        List<SolicitudEliminacion> solis;
        SolicitudEliminacion sol;
        Contribuyente contribuyente;
        String nuevoEstado = revisionSolicitudInputDto.getNuevoEstado();
        String adminId = revisionSolicitudInputDto.getAdminId();
        try {
            solis = solicitudService.solicitudesRelacionadas(id);
            sol = solicitudService.obtenerSolicitud(id); // El primero es la solicitud a cambiar
            contribuyente = contribuyenteService.obtenerContribuyentePorId(adminId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
