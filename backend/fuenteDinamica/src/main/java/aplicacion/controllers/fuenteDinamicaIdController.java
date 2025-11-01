package aplicacion.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class fuenteDinamicaIdController {
    @Value("${fuente.id}")
    private String idFuente;
    @GetMapping("/fuenteDinamicaId")
    public ResponseEntity<String> obtenerId(){
        return ResponseEntity.ok(idFuente);
    }
}
