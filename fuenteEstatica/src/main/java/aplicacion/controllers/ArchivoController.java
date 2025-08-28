package aplicacion.controllers;

import aplicacion.services.ArchivoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/fuentesEstaticas")
public class ArchivoController {

    private final ArchivoService archivoService;

    public ArchivoController(ArchivoService archivoService) {
        this.archivoService = archivoService;
    }

    @PostMapping("/{id}/archivo")
    public ResponseEntity<String> subirArchivo(@RequestParam("file") MultipartFile file,
                                               @PathVariable("id") Long id) {
        try {
            archivoService.subirArchivoPendiente(id, file);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al subir archivo: " + e.getMessage());
        }
        return ResponseEntity.ok("Archivo subido correctamente a pendientes");
    }

    @PostMapping("/{id}/archivo/por-url")
    public ResponseEntity<String> subirArchivoPorUrl(@RequestBody String url,
                                                     @PathVariable("id") Long id) {
        try {
            url = url.replaceAll("^\"|\"$", "");
            archivoService.subirArchivoDesdeUrl(id, url);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al subir archivo desde URL: " + e.getMessage());
        }
        return ResponseEntity.ok("Archivo subido correctamente desde URL a pendientes");
    }

    @PostMapping("/{id}/archivos")
    public ResponseEntity<String> subirArchivos(@RequestParam("files") MultipartFile[] files,
                                                @PathVariable("id") Long id) {
        try {
            for (MultipartFile file : files) {
                archivoService.subirArchivoPendiente(id, file);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al subir archivos: " + e.getMessage());
        }
        return ResponseEntity.ok("Archivos subidos correctamente a pendientes");
    }
}
