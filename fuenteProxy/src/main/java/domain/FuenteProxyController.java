package domain;

import domain.fuentesMetamapa.FuenteMetamapa;
import domain.hechos.Hecho;

import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

import java.util.List;


@RestController
@RequestMapping()
public class FuenteProxyController {
    private final FuenteMetamapa fuente = new FuenteMetamapa();

    @GetMapping("/hechos")
    public List<Hecho> obtenerHechos(@RequestParam(required=false) String categoria
                                     /*@RequestParam(required=false) String fecha_reporte_desde,
                                     @RequestParam(required=false) String fecha_reporte_hasta,
                                     @RequestParam(required=false) String fecha_acontecimiento_desde,
                                     @RequestParam(required=false) String fecha_acontecimiento_hasta,
                                     @RequestParam(required=false) String ubicacion*/) {
        return fuente.obtenerHechos().stream()
                .filter(h -> categoria == null || h.getCategoria().getNombre().equalsIgnoreCase(categoria))
                /* .filter(h -> fecha_reporte_desde == null  h.getUbicacion_acontecimiento().getNombre().equalsIgnoreCase(ubicacion))
                .filter(h -> fecha_reporte_hasta == null  h.getFecha_acontecimiento().toLocalDate().equals())
                .filter(h->fecha_acontecimiento_desde ==null || h.getFecha_acontecimiento().toLocalDate().equals(fecha_acontecimiento_desde))*/
                .collect(Collectors.toList());
    }
    //http://localhost:8082/hechos?categoria=robo
/*
    @GetMapping("/colecciones/{identificador}/hechos")
    public Hecho obtenerHechosColeccion(@PathVariable String identificador) {
        return fuente.obtenerHechosColeccion(identificador);
    }*/
}