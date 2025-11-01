package aplicacion.controllers;

import aplicacion.dto.input.FuenteProxyInputDto;
import aplicacion.dto.output.FuenteDisponibleOutputDto;
import aplicacion.dto.output.FuenteProxyOutputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.services.excepciones.FuenteNoEncontradaException;

import aplicacion.services.FuenteProxyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuentesProxy")
public class FuenteProxyController {
    private final FuenteProxyService fuenteProxyService;

    public FuenteProxyController(FuenteProxyService fuenteProxyService) {
        this.fuenteProxyService = fuenteProxyService;
    }

    @GetMapping("/hechos")
    public List<HechoOutputDto> obtenerHechos(){
        return fuenteProxyService.importarHechos();
    }

    @GetMapping("/{id}/hechos")
    public ResponseEntity<?> obtenerHechosDeFuente(@PathVariable("id") String id){
        try {
            return ResponseEntity.ok(fuenteProxyService.importarHechosDeFuente(id));
        } catch (FuenteNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<FuenteProxyOutputDto> guardarFuente(@RequestBody FuenteProxyInputDto fuenteProxyInputDto){
        FuenteProxyOutputDto fuenteProxy = fuenteProxyService.guardarFuente(fuenteProxyInputDto);
        return ResponseEntity.ok(fuenteProxy);
    }

    @GetMapping
    public ResponseEntity<List<FuenteDisponibleOutputDto>> obtenerFuentesDisponibles(){
        return ResponseEntity.ok(fuenteProxyService.obtenerFuentesDisponibles());
    }
}