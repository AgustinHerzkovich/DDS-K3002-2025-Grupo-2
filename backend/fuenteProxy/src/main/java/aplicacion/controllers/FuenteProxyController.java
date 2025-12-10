package aplicacion.controllers;

import aplicacion.dto.input.FuenteDemoInputDto;
import aplicacion.dto.input.FuenteMetamapaInputDto;
import aplicacion.dto.output.FuenteDemoOutputDto;
import aplicacion.dto.output.FuenteMetamapaOutputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.excepciones.FuenteNoEncontradaException;

import aplicacion.services.FuenteProxyService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
public class FuenteProxyController {
    private final FuenteProxyService fuenteProxyService;

    public FuenteProxyController(FuenteProxyService fuenteProxyService) {
        this.fuenteProxyService = fuenteProxyService;
    }

    @GetMapping("/hechos")
    public List<HechoOutputDto> obtenerHechos(@RequestParam(value = "fechaMayorA", required = false)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaMayorA){
        if (fechaMayorA != null) {
            return fuenteProxyService.importarHechosConFechaMayorA(fechaMayorA);
        }
        return fuenteProxyService.importarHechos();
    }

    @GetMapping("/{id}/hechos")
    public ResponseEntity<?> obtenerHechosDeFuente(@PathVariable(name = "id") String id){
        try {
            return ResponseEntity.ok(fuenteProxyService.importarHechosDeFuente(id));
        } catch (FuenteNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/fuentesDemo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FuenteDemoOutputDto> guardarFuenteDemo(@Valid @RequestBody FuenteDemoInputDto fuenteDemoInputDto){
        FuenteDemoOutputDto fuenteDemo = fuenteProxyService.guardarFuenteDemo(fuenteDemoInputDto);
        return ResponseEntity.status(201).body(fuenteDemo);
    }

    @PostMapping("/fuentesMetamapa")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FuenteMetamapaOutputDto> guardarFuenteMetamapa(@Valid @RequestBody FuenteMetamapaInputDto fuenteMetamapaInputDto){
        FuenteMetamapaOutputDto fuenteMetamapa = fuenteProxyService.guardarFuenteMetamapa(fuenteMetamapaInputDto);
        return ResponseEntity.status(201).body(fuenteMetamapa);
    }

    @GetMapping
    public ResponseEntity<List<String>> obtenerFuentesDisponibles(){
        return ResponseEntity.ok(fuenteProxyService.obtenerFuentesDisponibles());
    }
}