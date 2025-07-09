package domain.controllers;

import domain.fuentesProxy.fuentesMetamapa.FuenteMetamapa;
import domain.fuentesProxy.FuenteProxy;
import domain.hechos.Hecho;

import domain.services.FuenteProxyService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import java.util.List;


@RestController
@RequestMapping("/fuentesProxy")
@Component
public class FuenteProxyController {
    private final Map<Long, FuenteProxy> fuentes = new HashMap<>();
    private final FuenteProxyService fuenteProxyService;

    public FuenteProxyController(FuenteProxyService fuenteProxyService) {
        this.fuenteProxyService = fuenteProxyService;
        FuenteMetamapa fuente = new FuenteMetamapa(1L);

        fuentes.put(fuente.getId(), fuente);
    }

    @GetMapping("/hechos")
    public List<Hecho> obtenerHechos(){
        return fuenteProxyService.importarHechos();
    }

    @GetMapping("/colecciones/{id}/hechos")
    public List<Hecho> obtenerHechosPorColeccion(@PathVariable("id") Long idColeccion) {
        return fuenteProxyService.obtenerHechosColeccion(idColeccion);
    }
}