package aplicacion.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
public class HealthController {
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> checkHealth(){
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
