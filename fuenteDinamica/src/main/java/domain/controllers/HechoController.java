package domain.controllers;

import domain.dto.HechoDTO;
import domain.hechos.Hecho;
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
            return hechoService.obtenerHechosConFechaMayorA(fechaMayorA);
        }

        return hechoService.obtenerHechos();
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
}
