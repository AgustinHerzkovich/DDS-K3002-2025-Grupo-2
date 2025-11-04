package aplicacion.controllers;

import aplicacion.dto.input.FuenteAliasDto;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.mappers.FuenteOutputMapper;
import aplicacion.dto.output.FuenteOutputDto;
import aplicacion.excepciones.InvalidPageException;
import aplicacion.services.FuenteService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/agregador")
public class FuenteController {
    private final FuenteService fuenteService;
    private final FuenteOutputMapper fuenteOutputMapper = new FuenteOutputMapper();
    public FuenteController(FuenteService fuenteService) {
        this.fuenteService = fuenteService;
    }

    @GetMapping("/fuentes")
    public ResponseEntity<Page<FuenteOutputDto>> getFuentes(@RequestParam(value = "tipo", required = false) String tipoFuente, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit){
        InvalidPageException.validate(page, limit);
        return ResponseEntity.ok(fuenteService.findByTipo(page, limit, tipoFuente).map(fuenteOutputMapper::map));
    }
    @PatchMapping("/fuentes/{id}")
    public ResponseEntity<FuenteOutputDto> renameFuente(@RequestBody FuenteAliasDto fuenteAliasDto, @PathVariable(name = "id") String id){
        return ResponseEntity.ok(fuenteOutputMapper.map(fuenteService.cambiarAlias(id, fuenteAliasDto)));
    }

}
