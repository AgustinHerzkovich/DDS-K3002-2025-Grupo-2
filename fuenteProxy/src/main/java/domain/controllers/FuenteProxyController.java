package domain.controllers;

import domain.fuentesProxy.fuentesMetamapa.FuenteMetamapa;
import domain.fuentesProxy.FuenteProxy;
import domain.hechos.Hecho;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.List;


@RestController
@RequestMapping("/fuentesProxy")
@Component
public class FuenteProxyController {
    private final Map<Long, FuenteProxy> fuentes = new HashMap<>();

    public FuenteProxyController() {
        FuenteMetamapa fuente = new FuenteMetamapa(1L);

        fuentes.put(fuente.getId(), fuente);
    }
    @Scheduled(cron = "0 0 * * * *")  // Ejecuta al minuto 0 de cada hora
    public void pedirHechosCadaUnaHora() {
        fuentes.forEach( (idNoImporta, fuente) -> {fuente.pedirHechos();}); // TODO esto no es responsabilidad de ProxyController
        //todo necesitamos crear una clase nueva para realizar esta tarea
    }

    @GetMapping("/{id}/hechos")
    public List<Hecho> obtenerHechos(@PathVariable("id") Long id,
                                     @RequestParam(required=false) String categoria_buscada,
                                     @RequestParam(required=false) LocalDateTime fechaReporteDesde,
                                     @RequestParam(required=false) LocalDateTime fechaReporteHasta,
                                     @RequestParam(required=false) LocalDateTime fechaAcontecimientoDesde,
                                     @RequestParam(required=false) LocalDateTime fechaAcontecimientoHasta,
                                     @RequestParam(required=false) Double latitud,
                                     @RequestParam(required=false) Double longitud) {
        List<Hecho> hechosObtenidos = fuentes.get(id).importarHechos();
        return filtrarHechos(hechosObtenidos, categoria_buscada, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud);
    }
/*
    @GetMapping("/{id_fuente}/colecciones/{identificador}/hechos")
    public Hecho obtenerHechosColeccion(@PathVariable("id_fuente") Long id_fuente,
                                        @PathVariable("identificador") String identificador,
                                        @RequestParam(required=false) String categoria_buscada,
                                        @RequestParam(required=false) LocalDateTime fechaReporteDesde,
                                        @RequestParam(required=false) LocalDateTime fechaReporteHasta,
                                        @RequestParam(required=false) LocalDateTime fechaAcontecimientoDesde,
                                        @RequestParam(required=false) LocalDateTime fechaAcontecimientoHasta,
                                        @RequestParam(required=false) Double latitud,
                                        @RequestParam(required=false) Double longitud) {
        List<Hecho> hechosObtenidos =
        return filtrarHechos(hechosObtenidos, categoria_buscada, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud);
    }
*/
    public List<Hecho> filtrarHechos(List<Hecho> hechos,
                                     String categoria_buscada,
                                     LocalDateTime fechaReporteDesde,
                                     LocalDateTime fechaReporteHasta,
                                     LocalDateTime fechaAcontecimientoDesde,
                                     LocalDateTime fechaAcontecimientoHasta,
                                     Double latitud,
                                     Double longitud) {
        return hechos.stream()
                .filter(h -> categoria_buscada == null || h.getCategoria().getNombre().equalsIgnoreCase(categoria_buscada))
                .filter(h -> fechaReporteDesde == null ||  h.seCargoDespuesDe(fechaReporteDesde))
                .filter(h -> fechaReporteHasta == null || h.seCargoAntesDe(fechaReporteHasta))
                .filter(h -> fechaAcontecimientoDesde == null || h.ocurrioDespuesDe(fechaAcontecimientoDesde))
                .filter(h -> fechaAcontecimientoHasta == null || h.ocurrioAntesDe(fechaAcontecimientoHasta))
                .filter(h -> latitud == null || h.getUbicacion().getLatitud().equals(latitud))
                .filter(h -> longitud == null || h.getUbicacion().getLongitud().equals(longitud))
                .collect(Collectors.toList()); //convierte el stream de elementos (después de aplicar los .filter(...), .map(...), etc.) en una lista (List<T>) de resultados.
    }
}