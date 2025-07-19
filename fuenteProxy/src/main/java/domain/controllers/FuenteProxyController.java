package domain.controllers;

import domain.fuentesProxy.FuenteProxy;
import domain.fuentesProxy.fuentesMetamapa.FuenteMetamapa;
import domain.hechos.Hecho;

import domain.services.FuenteProxyService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/fuentesProxy")
@Component
public class FuenteProxyController {
    private final FuenteProxyService fuenteProxyService;

    public FuenteProxyController(FuenteProxyService fuenteProxyService) {
        this.fuenteProxyService = fuenteProxyService;
    }

    @GetMapping("/hechos")
    public List<Hecho> obtenerHechos(){
        return fuenteProxyService.importarHechos();
    }
    @PostMapping("/fuente")
    public ResponseEntity<Void> guardarFuente(@RequestBody FuenteMetamapa fuente){
        fuenteProxyService.guardarFuente(fuente);
        return ResponseEntity.ok().build();
    }
}