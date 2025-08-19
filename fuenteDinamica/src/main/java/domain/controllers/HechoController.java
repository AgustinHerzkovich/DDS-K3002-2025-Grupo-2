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

    @GetMapping("/{id}/hechos") //Se ejecuta al hacer GET en este id
    public List<Hecho> obtenerHechosDeFuente(
            @PathVariable("id") Long id,
            @RequestParam(value = "fechaMayorA", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime fechaMayorA
    ) {
        if (fechaMayorA != null) {
            return hechoService.obtenerHechosDeFuenteConFechaMayorA(id, fechaMayorA);
        }

        return hechoService.obtenerHechosDeFuente(id);
    }
}
