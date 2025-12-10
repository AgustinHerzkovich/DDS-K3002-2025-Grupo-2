package aplicacion.controllers;

import aplicacion.dto.output.HechoOutputDto;
import aplicacion.services.ArchivoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
public class HechoController {
    private final ArchivoService archivoService;

    public HechoController(ArchivoService archivoService) {
        this.archivoService = archivoService;
    }

    @GetMapping("/{fuente}/hechos")
    public ResponseEntity<List<HechoOutputDto>> obtenerHechos(@PathVariable(name = "fuente") String fuente,
                                                              @RequestParam(value = "fechaMayorA", required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime fechaMayorA) {
        if (fechaMayorA == null) {
            return ResponseEntity.ok(archivoService.leerHechos(fuente));
        } else {
            return ResponseEntity.ok(archivoService.leerHechosConFechaMayorA(fuente, fechaMayorA));
        }
    }
}