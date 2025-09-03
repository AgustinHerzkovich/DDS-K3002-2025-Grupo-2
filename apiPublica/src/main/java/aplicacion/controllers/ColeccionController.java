package aplicacion.controllers;

import aplicacion.config.ConfigService;
import aplicacion.helpers.UrlHelper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiPublica")
public class ColeccionController {
    private ConfigService configService;
    private final String urlBaseAgregador;
    private final SolicitudesHttp solicitudesHttp;

    public ColeccionController(ConfigService configService) {
        this.configService = configService;
        this.urlBaseAgregador = configService.getUrl();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    // --- READ ---
    @GetMapping("/colecciones/{id}/hechosIrrestrictos")
    public ResponseEntity<Object> mostrarHechosIrrestrictos(
            @PathVariable String id,
            @RequestParam(name = "categoria_buscada", required = false) String categoria_buscada,
            @RequestParam(name = "fechaReporteDesde", required = false) String fechaReporteDesde,
            @RequestParam(name = "fechaReporteHasta", required = false) String fechaReporteHasta,
            @RequestParam(name = "fechaAcontecimientoDesde", required = false) String fechaAcontecimientoDesde,
            @RequestParam(name = "fechaAcontecimientoHasta", required = false) String fechaAcontecimientoHasta,
            @RequestParam(name = "latitud", required = false) Double latitud,
            @RequestParam(name = "longitud", required = false) Double longitud
    )
    {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/colecciones/" + id + "/hechosIrrestrictos");
        UrlHelper.appendAllQueryParams(url, categoria_buscada, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud != null ? latitud.toString() : null, longitud != null ? longitud.toString() : null);
        return solicitudesHttp.get(url.toString(), Object.class);
    }

    @GetMapping("/colecciones/{id}/hechosCurados")
    public ResponseEntity<Object> mostrarHechosCurados(
            @PathVariable String id,
            @RequestParam(name = "categoria_buscada", required = false) String categoria_buscada,
            @RequestParam(name = "fechaReporteDesde", required = false) String fechaReporteDesde,
            @RequestParam(name = "fechaReporteHasta", required = false) String fechaReporteHasta,
            @RequestParam(name = "fechaAcontecimientoDesde", required = false) String fechaAcontecimientoDesde,
            @RequestParam(name = "fechaAcontecimientoHasta", required = false) String fechaAcontecimientoHasta,
            @RequestParam(name = "latitud", required = false) Double latitud,
            @RequestParam(name = "longitud", required = false) Double longitud
    ) {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/colecciones/" + id + "/hechosCurados");
        UrlHelper.appendAllQueryParams(url, categoria_buscada, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud != null ? latitud.toString() : null, longitud != null ? longitud.toString() : null);
        return solicitudesHttp.get(url.toString(), Object.class);
    }
}