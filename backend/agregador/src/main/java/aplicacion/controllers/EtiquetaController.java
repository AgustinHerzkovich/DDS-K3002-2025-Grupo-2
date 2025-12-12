package aplicacion.controllers;

import aplicacion.excepciones.TooHighLimitException;
import aplicacion.services.EtiquetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class EtiquetaController {
    private final EtiquetaService etiquetaService;

    public EtiquetaController(EtiquetaService etiquetaService) {
        this.etiquetaService = etiquetaService;
    }

    @GetMapping("/etiquetas/index")
    public ResponseEntity<List<String>> autoCompletar(@RequestParam(name = "search") String currentSearch, @RequestParam(name = "limit", required = false, defaultValue = "5") Integer limit){
        if(limit >100 || limit <0)
            throw new TooHighLimitException(limit);
        List<String> recomendaciones = etiquetaService.obtenerAutocompletado(currentSearch, limit);
        return ResponseEntity.ok(recomendaciones);
    }
}
