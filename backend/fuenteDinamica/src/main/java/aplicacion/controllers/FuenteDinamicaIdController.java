package aplicacion.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FuenteDinamicaIdController {
    @Value("${fuente.id}")
    private String idFuente;
    @GetMapping
    public ResponseEntity<List<String>> obtenerId(){
        return ResponseEntity.ok(List.of(idFuente));
    }
}
