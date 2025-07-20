package domain.controllers;

import domain.fuentesEstaticas.FuenteEstatica;
import domain.hechos.Hecho;
import domain.services.FuenteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController // Le decimos que esta clase es un controlador REST
// Esto significa que es lo que *expone la api* para que sea consumido
@RequestMapping("/fuentesEstaticas") // Define ruta base para todos los endpoints de esta clase
public class FuenteController {
    private final FuenteService fuenteService;

    public FuenteController(FuenteService fuenteService) {
        this.fuenteService = fuenteService;
    }

    @GetMapping("/hechos")
    public List<Hecho> obtenerTodosLosHechos(
            @RequestParam(value = "fechaMayorA", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime fechaMayorA
            ) {
        return fuenteService.obtenerTodosLosHechosConFechaMayorA(fechaMayorA);
    }

    @GetMapping("/{id}/hechos") //Se ejecuta al hacer GET en este id
    public List<Hecho> obtenerHechosPorFuente(
            @PathVariable("id") Long id,
            @RequestParam(value = "fechaMayorA", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime fechaMayorA
            ) {
        return fuenteService.obtenerHechosPorFuenteConFechaMayorA(id, fechaMayorA);
    }

    @PostMapping
    public ResponseEntity<FuenteEstatica> crearFuente(@RequestBody List<String> archivos) {
        FuenteEstatica nuevaFuente = fuenteService.crearFuenteEstatica(archivos);
        System.out.println("Se ha creado una nueva fuente estática: " + nuevaFuente.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaFuente);
    }
}