package aplicacion.controllers;

import aplicacion.domain.FuenteProxy;
import aplicacion.services.excepciones.FuenteNoEncontradaException;
import domain.hechos.Hecho;

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
    public List<Hecho> obtenerHechos(){
        return fuenteProxyService.importarHechos();
    }

    @GetMapping("/{id}/hechos")
    public ResponseEntity<List<Hecho>> obtenerHechosDeFuente(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(fuenteProxyService.importarHechosDeFuente(id));
        } catch (FuenteNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/fuente")
    public ResponseEntity<Void> guardarFuente(@RequestBody FuenteProxy fuente){
        fuenteProxyService.guardarFuente(fuente);
        return ResponseEntity.ok().build();
    }
}