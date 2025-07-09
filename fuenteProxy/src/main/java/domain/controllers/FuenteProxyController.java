package domain.controllers;

import domain.hechos.Hecho;

import domain.services.FuenteProxyService;
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
}