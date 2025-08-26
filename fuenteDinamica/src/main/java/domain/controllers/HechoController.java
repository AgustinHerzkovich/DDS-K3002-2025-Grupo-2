package domain.controllers;

import domain.dto.HechoDTO;
import domain.dto.HechoEdicionDTO;
import domain.hechos.EstadoRevision;
import domain.hechos.Hecho;
import domain.hechos.RevisionHecho;
import domain.services.HechoService;
import org.springframework.format.annotation.DateTimeFormat;
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
    public Hecho obtenerHecho(@PathVariable("id") String id) {
        return hechoService.obtenerHecho(id);
    }

    @PostMapping("/hechos")
    public ResponseEntity<Void> agregarHecho(@RequestBody HechoDTO hechoDto) {
        Hecho hecho = hechoService.guardarHechoDto(hechoDto);
        System.out.println("Se ha agregado el hecho: " + hecho.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/hechos/{id}/estadoRevision") // TODO: Cuando tengamos acceso a datos de la sesión del administrador, se debe registrar la revisión en la clase RevisionHecho
    public ResponseEntity<Void> modificarEstadoRevision(@PathVariable("id") String id,
                                                        @RequestBody EstadoRevision nuevoEstado) {
        try {
            Hecho hecho = hechoService.modificarEstadoRevision(id, nuevoEstado);
            System.out.println("Se ha modificado el estado de revisión del hecho " + hecho.getTitulo() + "(" + id + ")" + " a " + nuevoEstado);
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
        } catch (Exception e) {
            return ResponseEntity.notFound().header("hecho", "Kaputt").build();
        }
    }


}
