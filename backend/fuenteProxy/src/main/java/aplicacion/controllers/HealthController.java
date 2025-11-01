package aplicacion.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping
    public ResponseEntity<Map<String, String>> checkHealth(){
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
