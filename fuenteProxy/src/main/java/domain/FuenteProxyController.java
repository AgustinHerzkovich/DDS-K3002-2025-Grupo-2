package domain;

import domain.fuentesMetamapa.FuenteMetamapa;
import domain.hechos.Hecho;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.stream.Collectors;

import java.util.List;


@RestController
@RequestMapping()
public class FuenteProxyController {
    private final FuenteMetamapa fuente = new FuenteMetamapa();

    @GetMapping("/hechos")
    public List<Hecho> obtenerHechos(@RequestParam(required=false) String categoria_buscada,
                                     @RequestParam(required=false) LocalDate fecha_reporte_desde,
                                     @RequestParam(required=false) LocalDate fecha_reporte_hasta,
                                     @RequestParam(required=false) LocalDate fecha_acontecimiento_desde,
                                     @RequestParam(required=false) LocalDate fecha_acontecimiento_hasta,
                                     @RequestParam(required=false) Double latitud,
                                     @RequestParam(required=false) Double longitud) {
        return fuente.obtenerHechos().stream()
                .filter(h -> categoria_buscada == null || h.getCategoria().getNombre().equalsIgnoreCase(categoria_buscada))
                .filter(h -> fecha_reporte_desde == null ||  h.seCargoDespuesDe(fecha_reporte_desde))
                .filter(h -> fecha_reporte_hasta == null || h.seCargoAntesDe(fecha_reporte_hasta))
                .filter(h -> fecha_acontecimiento_desde == null || h.ocurrioDespuesDe(fecha_acontecimiento_desde))
                .filter(h -> fecha_acontecimiento_hasta == null || h.ocurrioAntesDe(fecha_acontecimiento_hasta))
                .filter(h -> latitud == null || h.getUbicacion().getLatitud().equals(latitud))
                .filter(h -> longitud == null || h.getUbicacion().getLongitud().equals(longitud))
                .collect(Collectors.toList()); //convierte el stream de elementos (después de aplicar los .filter(...), .map(...), etc.) en una lista (List<T>) de resultados.
    }

    //http://localhost:8082/hechos?categoria=robo&ubicacion={123222,123232}
/*
    @GetMapping("/colecciones/{identificador}/hechos")
    public Hecho obtenerHechosColeccion(@PathVariable String identificador) {
        return fuente.obtenerHechosColeccion(identificador);
    }*/
}