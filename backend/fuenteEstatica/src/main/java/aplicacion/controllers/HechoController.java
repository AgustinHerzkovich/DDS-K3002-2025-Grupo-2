package aplicacion.controllers;

import aplicacion.dto.output.HechoOutputDto;
import aplicacion.services.ArchivoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/fuentesEstaticas")
public class HechoController {
    private final ArchivoService archivoService;

    public HechoController(ArchivoService archivoService) {
        this.archivoService = archivoService;
    }

    @GetMapping("/{fuente}/hechos")
    public List<HechoOutputDto> obtenerHechos(@PathVariable("fuente") String fuente,
                                              @RequestParam(value = "fechaMayorA", required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime fechaMayorA) {
        if (fechaMayorA == null) {
            return archivoService.leerHechos(fuente);
        } else {
            return archivoService.leerHechosConFechaMayorA(fuente, fechaMayorA);
        }
    }
}