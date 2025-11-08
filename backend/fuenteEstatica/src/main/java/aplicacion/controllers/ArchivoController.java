package aplicacion.controllers;

import aplicacion.services.ArchivoService;
import aplicacion.services.AwsS3FileServerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/fuentesEstaticas")
public class ArchivoController {

    private final ArchivoService archivoService;
    private final AwsS3FileServerService awsS3FileServerService;
    public ArchivoController(ArchivoService archivoService, AwsS3FileServerService awsS3FileServerService) {
        this.archivoService = archivoService;
        this.awsS3FileServerService = awsS3FileServerService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getFuentesEstaticas(){
        return ResponseEntity.ok(awsS3FileServerService.listarFuentes());
    }

    @PostMapping("/archivos/por-url")
    public ResponseEntity<String> subirArchivoPorUrl(@RequestBody String url) {
        try {
            url = url.replaceAll("^\"|\"$", "");
            archivoService.subirArchivoDesdeUrl(url);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al subir archivo desde URL: " + e.getMessage());
        }
        return ResponseEntity.ok("Archivo subido correctamente desde URL al FileServer");
    }

    @PostMapping("/archivos")
    public ResponseEntity<String> subirArchivos(@RequestParam("files") MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {
                archivoService.subirArchivo(file);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al subir archivos: " + e.getMessage());
        }
        return ResponseEntity.ok("Archivos subidos correctamente al FileServer");
    }
}
