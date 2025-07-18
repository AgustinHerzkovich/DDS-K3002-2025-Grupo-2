package domain.controllers;

import domain.fuentesDinamicas.FuenteDinamica;
import domain.hechos.Categoria;
import domain.hechos.Hecho;
import domain.hechos.Origen;
import domain.repositorios.RepositorioDeHechos;
import domain.repositorios.RepositorioDeSolicitudes;
import domain.services.FuenteService;
import domain.solicitudes.DetectorDeSpamPrueba;
import domain.solicitudes.SolicitudEliminacion;
import domain.usuarios.Contribuyente;
import domain.usuarios.IdentidadContribuyente;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


// TODO: Implementar posteo de solicitudes de eliminacion
// HechoController
// SolicitudController
// HechoService
// SolicitudService
// Persistencia de Hechos y Solicitudes
@RestController // Le decimos que esta clase es un controlador REST
// Esto significa que es lo que *expone la api* para que sea consumido
@RequestMapping("/fuentesDinamicas") // Define ruta base para todos los endpoints de esta clase
public class FuenteController {
    private final FuenteService fuenteService;

    public FuenteController(FuenteService fuenteService) {
        this.fuenteService = fuenteService;
    }

    @PostMapping
    public ResponseEntity<FuenteDinamica> crearFuente() {
        FuenteDinamica nuevaFuente = fuenteService.guardarFuente();
        System.out.println("Se ha creado una nueva fuente dinámica: " + nuevaFuente.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaFuente);
    }

    @DeleteMapping
    public ResponseEntity<FuenteDinamica> eliminarFuente(@RequestBody FuenteDinamica fuente) {
        fuenteService.eliminarFuente(fuente);
        System.out.println("Se ha eliminado la fuente dinámica: " + fuente.getId());
        return ResponseEntity.status(HttpStatus.OK).body(fuente);
    }
}
