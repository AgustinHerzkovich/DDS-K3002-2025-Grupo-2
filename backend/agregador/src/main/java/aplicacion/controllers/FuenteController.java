package aplicacion.controllers;

import aplicacion.dto.mappers.FuenteOutputMapper;
import aplicacion.dto.output.FuenteOutputDto;
import aplicacion.excepciones.InvalidPageException;
import aplicacion.services.FuenteService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/fuentes")
public class FuenteController {
    private final FuenteService fuenteService;
    private final FuenteOutputMapper fuenteOutputMapper = new FuenteOutputMapper();
    public FuenteController(FuenteService fuenteService) {
        this.fuenteService = fuenteService;
    }

    @GetMapping
    public ResponseEntity<Page<FuenteOutputDto>> getFuentes(@RequestParam(value = "tipo", required = false) String tipoFuente, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit){
        InvalidPageException.validate(page, limit);
        return ResponseEntity.ok(fuenteService.findByTipo(page, limit, tipoFuente).map(fuenteOutputMapper::map));
    }

}
