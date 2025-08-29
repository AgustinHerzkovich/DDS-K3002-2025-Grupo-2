package aplicacion.controllers;

import aplicacion.services.dto.CambioEstadoRevisionDto;
import aplicacion.services.dto.HechoDTO;
import aplicacion.services.dto.HechoEdicionDTO;
import aplicacion.domain.hechos.Hecho;
import aplicacion.services.HechoService;
import aplicacion.services.excepciones.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fuentesDinamicas")
public class HechoController {
    private final HechoService hechoService;

    public HechoController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping("/hechos")
    public List<Hecho> obtenerHechos(
            @RequestParam(value = "fechaMayorA", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime fechaMayorA
    ) {
        if (fechaMayorA != null) {
            return hechoService.obtenerHechosAceptadosConFechaMayorA(fechaMayorA);
        }

        return hechoService.obtenerHechosAceptados();
    }

    @GetMapping("/hechos/{id}")
    public ResponseEntity<Hecho> obtenerHecho(@PathVariable("id") String id) {

        try{
            Hecho hecho = hechoService.obtenerHecho(id);
            return ResponseEntity.ok(hecho);
        }catch (HechoNoEncontradoException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/hechos")
    public ResponseEntity<?> agregarHecho(@RequestBody HechoDTO hechoDto) {
        Hecho hecho;
        try {
            hecho = hechoService.guardarHechoDto(hechoDto);
        }catch (ContribuyenteNoConfiguradoException e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of("error", "Contributente no configurado", "message", e.getMessage()));
        }catch (ContribuyenteAssignmentException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al asignar post al contribuyente", "message", e.getMessage()));
        }catch (HechoMappingException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Hecho mal armado", "message", e.getMessage()));
        }catch (HechoStorageException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "No se pudo almacenar el hecho en la base de datos", "message", e.getMessage()));
        }
        System.out.println("Se ha agregado el hecho: " + hecho.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/hechos/{id}/estadoRevision") // TODO: Cuando tengamos acceso a datos de la sesión del administrador, se debe registrar la revisión en la clase RevisionHecho
    public ResponseEntity<Void> modificarEstadoRevision(@PathVariable("id") String id,
                                                        @RequestBody CambioEstadoRevisionDto cambioEstadoRevisionDto) {
        try {
            Hecho hecho = hechoService.modificarEstadoRevision(id, cambioEstadoRevisionDto);
            System.out.println("Se ha modificado el estado de revisión del hecho " + hecho.getTitulo() + "(" + id + ")" + " a " + cambioEstadoRevisionDto.getEstado());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/hechos/{id}")
    public ResponseEntity<Void> editarHecho(@PathVariable("id") String id,
                                            @RequestBody HechoEdicionDTO hechoEdicionDto) {
        try {
            Hecho hecho = hechoService.editarHecho(id, hechoEdicionDto);
            System.out.println("Se ha editado correctamente el hecho: " + hecho.getTitulo() + "(" + id + ")");
            return ResponseEntity.ok().build();
        } catch (HechoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        } catch (PlazoEdicionVencidoException e){
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        } catch (AnonimatoException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
