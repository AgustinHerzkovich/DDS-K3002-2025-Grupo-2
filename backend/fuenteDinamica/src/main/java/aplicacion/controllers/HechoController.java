package aplicacion.controllers;

import aplicacion.dto.input.CambioEstadoRevisionInputDto;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.input.HechoEdicionInputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.dto.output.HechoRevisadoOutputDto;
import aplicacion.services.HechoService;
import aplicacion.excepciones.*;
import domain.helpers.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/fuentesDinamicas")
public class HechoController {
    private final HechoService hechoService;

    public HechoController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping("/hechos")
    public ResponseEntity<Page<HechoOutputDto>> obtenerHechos(
            @RequestParam(value = "fechaMayorA", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaMayorA,
            @RequestParam(value = "pendiente", required = false, defaultValue = "false") Boolean pendiente,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        Page<HechoOutputDto> hechos;
        Pageable pageable = PageRequest.of(page, size);

        // Combinar filtros de fechaMayorA y pendiente
        if (fechaMayorA != null && pendiente) {
            // Hechos pendientes con fecha mayor a
            hechos = hechoService.obtenerHechosPendientesConFechaMayorA(fechaMayorA, pageable);
        } else if (fechaMayorA != null) {
            // Hechos aceptados con fecha mayor a
            hechos = hechoService.obtenerHechosAceptadosConFechaMayorA(fechaMayorA, pageable);
        } else if (pendiente) {
            // Todos los hechos pendientes
            hechos = hechoService.obtenerHechosPendientes(pageable);
        } else {
            // Todos los hechos aceptados
            hechos = hechoService.obtenerHechosAceptados(pageable);
        }

        return ResponseEntity.ok(hechos);
    }

    @GetMapping("/hechos/{id}")
    public ResponseEntity<?> obtenerHecho(@PathVariable(name = "id") String id) {
        try{
            HechoOutputDto hecho = hechoService.obtenerHecho(id);
            return ResponseEntity.ok(hecho);
        }catch (HechoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/hechos")
    public ResponseEntity<?> agregarHecho(@Valid @RequestBody HechoInputDto hechoInputDto) {
        HechoOutputDto hecho;
        try {
            hecho = hechoService.guardarHecho(hechoInputDto);
            System.out.println("Se ha agregado el hecho: " + hecho.getId());
            return ResponseEntity.status(201).body(hecho);
        }catch (ContribuyenteNoConfiguradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/hechos/{id}/estadoRevision")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> modificarEstadoRevision(@PathVariable(name = "id") String id,
                                                     @Valid @RequestBody CambioEstadoRevisionInputDto cambioEstadoRevisionInputDto,
                                                     @RequestHeader(value = "Administrador", required = false) String administradorId) {
        try {
            HechoRevisadoOutputDto hecho = hechoService.modificarEstadoRevision(id, cambioEstadoRevisionInputDto);
            if (administradorId != null) {
                hechoService.guardarRevision(id, administradorId);
            }
            System.out.println("Se ha modificado el estado de revisión del hecho " + hecho.getTitulo() + "(" + id + ")" + " a " + cambioEstadoRevisionInputDto.getEstado());
            return ResponseEntity.ok(hecho);
        } catch (HechoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/hechos/{id}")
    public ResponseEntity<?> editarHecho(@PathVariable(name = "id") String id,
                                         @Valid @RequestBody HechoEdicionInputDto hechoEdicionInputDto,
                                         @RequestHeader(value = "Authorization", required = false) String token) {
        System.out.println("EDITANDO el hecho: " + id );
        try {
            // Validar que el usuario sea el autor del hecho
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se proporcionó token de autenticación");
            }

            // Extraer el ID del usuario del token JWT
            String userId = JwtUtil.extractUserId(token);
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
            }

            // Si la validación pasa, proceder con la edición
            HechoOutputDto hecho = hechoService.editarHecho(id, hechoEdicionInputDto, userId);
            System.out.println("Se ha editado correctamente el hecho: " + hecho.getTitulo() + "(" + id + ")" + " por el usuario: " + userId);
            return ResponseEntity.ok(hecho);
        } catch (HechoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (PlazoEdicionVencidoException | AnonimatoException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (AutorizacionDenegadaException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
