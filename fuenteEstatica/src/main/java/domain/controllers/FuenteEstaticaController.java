package domain.controllers;

import domain.fuentesEstaticas.Fuente;
import domain.fuentesEstaticas.FuenteEstatica;
import domain.fuentesEstaticas.LectorCsv;
import domain.hechos.Hecho;
import domain.services.FuenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import java.io.IOException;

import java.util.*;

@RestController // Le decimos que esta clase es un controlador REST
// Esto significa que es lo que *expone la api* para que sea consumido
@RequestMapping("/fuentesEstaticas") // Define ruta base para todos los endpoints de esta clase
public class FuenteEstaticaController {
    private final FuenteService fuenteService;

    public FuenteEstaticaController(FuenteService fuenteService) {
        this.fuenteService = fuenteService;
    }

    @GetMapping
    public List<Hecho> obtenerTodosLosHechos() {
        return fuenteService.obtenerTodosLosHechos();
    }

    @GetMapping("/{id}/hechos") //Se ejecuta al hacer GET en este id
    public List<Hecho> obtenerHechosPorFuente(@PathVariable("id") Long id) {
        return fuenteService.obtenerHechosPorFuente(id);
    }

    @PostMapping
    public ResponseEntity<FuenteEstatica> crearFuente(@RequestBody List<String> archivos) {
        FuenteEstatica nuevaFuente = fuenteService.crearFuenteEstatica(archivos);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaFuente);
    }
}