package domain.controllers;

import domain.hechos.Hecho;
import domain.services.ArchivoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/fuentesEstaticas")
public class ArchivoController {

    private final ArchivoService archivoService;

    public ArchivoController(ArchivoService archivoService) {
        this.archivoService = archivoService;
    }

    @PostMapping("/archivo")
    public ResponseEntity<String> subirArchivo(@RequestParam("file") MultipartFile file) {
        try {
            archivoService.subirArchivoPendiente(file);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al subir archivo: " + e.getMessage());
        }
        return ResponseEntity.ok("Archivo subido correctamente a pendientes");
    }

    @PostMapping("/archivos")
    public ResponseEntity<String> subirArchivos(@RequestParam("files") MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {
                archivoService.subirArchivoPendiente(file);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al subir archivos: " + e.getMessage());
        }
        return ResponseEntity.ok("Archivos subidos correctamente a pendientes");
    }

    @GetMapping("/hechos")
    public List<Hecho> obtenerHechos(@RequestParam(value = "fechaMayorA", required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime fechaMayorA) {
        return archivoService.leerHechosPendientesConFechaMayorA(fechaMayorA);
    }
}
