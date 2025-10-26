package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.helpers.UrlHelper;
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
            @RequestParam(name = "categoria", required = false) String categoria,
            @RequestParam(name = "fechaReporteDesde", required = false) String fechaReporteDesde,
            @RequestParam(name = "fechaReporteHasta", required = false) String fechaReporteHasta,
            @RequestParam(name = "fechaAcontecimientoDesde", required = false) String fechaAcontecimientoDesde,
            @RequestParam(name = "fechaAcontecimientoHasta", required = false) String fechaAcontecimientoHasta,
            @RequestParam(name = "latitud", required = false) Double latitud,
            @RequestParam(name = "longitud", required = false) Double longitud,
            @RequestParam(name = "search", required = false) String textoLibre
    )
    {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/colecciones/" + id + "/hechosIrrestrictos");
        UrlHelper.appendQueryParam(url, "categoria", categoria);
        UrlHelper.appendQueryParam(url, "fechaReporteDesde", fechaReporteDesde);
        UrlHelper.appendQueryParam(url, "fechaReporteHasta", fechaReporteHasta);
        UrlHelper.appendQueryParam(url, "fechaAcontecimientoDesde", fechaAcontecimientoDesde);
        UrlHelper.appendQueryParam(url, "fechaAcontecimientoHasta", fechaAcontecimientoHasta);
        UrlHelper.appendQueryParam(url, "latitud", latitud);
        UrlHelper.appendQueryParam(url, "longitud", longitud);
        UrlHelper.appendQueryParam(url, "search", textoLibre);
        return solicitudesHttp.get(url.toString(), Object.class);
    }

    @GetMapping("/colecciones/{id}/hechosCurados")
    public ResponseEntity<Object> mostrarHechosCurados(
            @PathVariable String id,
            @RequestParam(name = "categoria", required = false) String categoria,
            @RequestParam(name = "fechaReporteDesde", required = false) String fechaReporteDesde,
            @RequestParam(name = "fechaReporteHasta", required = false) String fechaReporteHasta,
            @RequestParam(name = "fechaAcontecimientoDesde", required = false) String fechaAcontecimientoDesde,
            @RequestParam(name = "fechaAcontecimientoHasta", required = false) String fechaAcontecimientoHasta,
            @RequestParam(name = "latitud", required = false) Double latitud,
            @RequestParam(name = "longitud", required = false) Double longitud,
            @RequestParam(name = "search", required = false) String textoLibre
    ) {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/colecciones/" + id + "/hechosCurados");
        UrlHelper.appendQueryParam(url, "categoria", categoria);
        UrlHelper.appendQueryParam(url, "fechaReporteDesde", fechaReporteDesde);
        UrlHelper.appendQueryParam(url, "fechaReporteHasta", fechaReporteHasta);
        UrlHelper.appendQueryParam(url, "fechaAcontecimientoDesde", fechaAcontecimientoDesde);
        UrlHelper.appendQueryParam(url, "fechaAcontecimientoHasta", fechaAcontecimientoHasta);
        UrlHelper.appendQueryParam(url, "latitud", latitud);
        UrlHelper.appendQueryParam(url, "longitud", longitud);
        UrlHelper.appendQueryParam(url, "search", textoLibre);
        return solicitudesHttp.get(url.toString(), Object.class);
    }

    // --- READ ---
    @GetMapping("/colecciones")
    public ResponseEntity<Object> mostrarColecciones(@RequestParam(name = "search", required = false) String textoBuscado,
                                                     @RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/colecciones");
        UrlHelper.appendQueryParam(url, "search", textoBuscado);
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "size", size);
        return solicitudesHttp.get(url.toString(), Object.class);
    }

    @GetMapping("/colecciones/{id}")
    public ResponseEntity<Object> mostrarColeccion(@PathVariable String id) {
        return solicitudesHttp.get(urlBaseAgregador + "/colecciones/" + id, Object.class);
    }
}