package aplicacion.controllers;

import aplicacion.excepciones.TooHighLimitException;
import aplicacion.services.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/categorias/index")
    public ResponseEntity<List<String>> autoCompletar(@RequestParam(name = "search") String currentSearch, @RequestParam(name = "limit", required = false, defaultValue = "5") Integer limit){
        if(limit >100 || limit <0)
            throw new TooHighLimitException(limit);
        List<String> recomendaciones = categoriaService.obtenerAutocompletado(currentSearch, limit);
        return ResponseEntity.ok(recomendaciones);
    }
}
