package aplicacion.controllers;

import aplicacion.dto.input.FuenteAliasDto;
import aplicacion.dto.mappers.FuenteOutputMapper;
import aplicacion.dto.output.AgregadorOutputDto;
import aplicacion.dto.output.FuenteOutputDto;
import aplicacion.excepciones.InvalidPageException;
import aplicacion.services.FuenteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping
public class FuenteController {
    private final FuenteService fuenteService;
    private final FuenteOutputMapper fuenteOutputMapper = new FuenteOutputMapper();
    public FuenteController(FuenteService fuenteService) {
        this.fuenteService = fuenteService;
    }

    @GetMapping("/fuentes")
    public ResponseEntity<Page<FuenteOutputDto>> getFuentes(@RequestParam(value = "tipo", required = false) String tipoFuente, @RequestParam(name = "page", defaultValue = "0") Integer page, @RequestParam(name = "limit", defaultValue = "10") Integer limit){
        InvalidPageException.validate(page, limit);
        return ResponseEntity.ok(fuenteService.findByTipo(page, limit, tipoFuente).map(fuenteOutputMapper::map));
    }
    @PatchMapping("/fuentes/{id}")
    public ResponseEntity<FuenteOutputDto> renameFuente(@Valid @RequestBody FuenteAliasDto fuenteAliasDto, @PathVariable(name = "id") String id){
        return ResponseEntity.ok(fuenteOutputMapper.map(fuenteService.cambiarAlias(id, fuenteAliasDto)));
    }
}
