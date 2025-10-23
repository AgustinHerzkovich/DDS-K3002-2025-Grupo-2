package aplicacion.controllers;

import aplicacion.config.ConfigService;
import aplicacion.helpers.UrlHelper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiPublica")
public class HechoController {
    private final String urlBaseAgregador;
    private final SolicitudesHttp solicitudesHttp;

    public HechoController(ConfigService configService) {
        this.urlBaseAgregador = configService.getUrl();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PostMapping("/hechos")
    public ResponseEntity<Object> reportarHecho(@RequestBody Object body) {
        return solicitudesHttp.post(urlBaseAgregador + "/hechos", body, Object.class);
    }

    @GetMapping("/hechos")
    public ResponseEntity<Object> obtenerHechos(
            @RequestParam(name = "categoria", required = false) String categoria,
            @RequestParam(name = "fechaReporteDesde", required = false) String fechaReporteDesde,
            @RequestParam(name = "fechaReporteHasta", required = false) String fechaReporteHasta,
            @RequestParam(name = "fechaAcontecimientoDesde", required = false) String fechaAcontecimientoDesde,
            @RequestParam(name = "fechaAcontecimientoHasta", required = false) String fechaAcontecimientoHasta,
            @RequestParam(name = "latitud", required = false) Double latitud,
            @RequestParam(name = "longitud", required = false) Double longitud,
            @RequestParam(name = "search", required = false) String textoLibre
    ) {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/hechos");
        UrlHelper.appendAllQueryParams(url, categoria, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud != null ? latitud.toString() : null, longitud != null ? longitud.toString() : null, textoLibre);
        return solicitudesHttp.get(url.toString(), Object.class);
    }

    @GetMapping("/hechos/{id}")
    public ResponseEntity<Object> obtenerHechoPorId(@PathVariable("id") String id) {
        String url = urlBaseAgregador + "/hechos/" + id;
        return solicitudesHttp.get(url, Object.class);
    }
}