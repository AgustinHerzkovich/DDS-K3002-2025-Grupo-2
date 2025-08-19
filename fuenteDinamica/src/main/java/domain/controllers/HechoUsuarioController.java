package domain.controllers;

import domain.hechos.Hecho;
import domain.services.HechoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/fuentesDinamicas")
public class HechoUsuarioController {
    private final HechoService hechoService;

    public HechoUsuarioController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @PostMapping("/{id}/hechos")
    public ResponseEntity<Void> agregarHecho(
            @PathVariable("id") Long id,
            @RequestBody Hecho hecho) {
        hechoService.guardarHechoEnFuente(id, hecho);
        System.out.println("Se ha agregado el hecho " + hecho.getId() + " a la fuente con id " + id);
        return ResponseEntity.ok().build();
    }
}
