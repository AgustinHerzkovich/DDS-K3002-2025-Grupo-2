package aplicacion.controllers;

import aplicacion.dto.input.CambioEstadoRevisionInputDto;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.input.HechoEdicionInputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.dto.output.HechoRevisadoOutputDto;
import aplicacion.services.HechoService;
import aplicacion.excepciones.*;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<HechoOutputDto>> obtenerHechos(
            @RequestParam(value = "fechaMayorA", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime fechaMayorA,
            @RequestParam(value = "pendiente", required = false, defaultValue = "false") Boolean pendiente
    ) {
        List<HechoOutputDto> hechos;
        if (fechaMayorA != null) {
            hechos = hechoService.obtenerHechosAceptadosConFechaMayorA(fechaMayorA);
        } else if(pendiente){
            hechos = hechoService.obtenerHechosPendientes();
        }else{
            hechos = hechoService.obtenerHechosAceptados();
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
            return ResponseEntity.ok(hecho);
        }catch (ContribuyenteNoConfiguradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/hechos/{id}/estadoRevision")
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
                                         @Valid @RequestBody HechoEdicionInputDto hechoEdicionInputDto) {
        System.out.println("EDITANDO el hecho: " + id );
        try {
            HechoOutputDto hecho = hechoService.editarHecho(id, hechoEdicionInputDto);
            System.out.println("Se ha editado correctamente el hecho: " + hecho.getTitulo() + "(" + id + ")");
            return ResponseEntity.ok(hecho);
        } catch (HechoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (PlazoEdicionVencidoException | AnonimatoException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
