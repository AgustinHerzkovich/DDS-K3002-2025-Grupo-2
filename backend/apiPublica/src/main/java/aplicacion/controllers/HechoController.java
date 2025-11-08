package aplicacion.controllers;

import aplicacion.config.ConfigService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import domain.helpers.UrlHelper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/apiPublica")
public class HechoController {
    private final String urlBaseAgregador;
    private final SolicitudesHttp solicitudesHttp;
    private final Cache<String, ResponseEntity<Object>> cache = Caffeine.newBuilder().maximumSize(100000).expireAfterWrite(1, TimeUnit.MINUTES).build();
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
            @RequestParam(name = "search", required = false) String textoLibre,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "100") Integer size
    ) {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/hechos");
        UrlHelper.appendQueryParam(url, "categoria", categoria);
        UrlHelper.appendQueryParam(url, "fechaReporteDesde", fechaReporteDesde);
        UrlHelper.appendQueryParam(url, "fechaReporteHasta", fechaReporteHasta);
        UrlHelper.appendQueryParam(url, "fechaAcontecimientoDesde", fechaAcontecimientoDesde);
        UrlHelper.appendQueryParam(url, "fechaAcontecimientoHasta", fechaAcontecimientoHasta);
        UrlHelper.appendQueryParam(url, "latitud", latitud);
        UrlHelper.appendQueryParam(url, "longitud", longitud);
        UrlHelper.appendQueryParam(url, "search", textoLibre);
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "size", size);
        return solicitudesHttp.get(url.toString(), Object.class);
    }

    @GetMapping("/hechos/{id}")
    public ResponseEntity<Object> obtenerHechoPorId(@PathVariable("id") String id) {
        String url = urlBaseAgregador + "/hechos/" + id;
        return solicitudesHttp.get(url, Object.class);
    }

    @GetMapping("/hechos/index")
    public ResponseEntity<Object> obtenerRecomendaciones(@RequestParam(name="search", required = true) String texto, @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limite) {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/hechos/index");
        UrlHelper.appendQueryParam(url, "search", texto);
        UrlHelper.appendQueryParam(url, "limit", limite);
        String key = texto + "|" + limite;
        ResponseEntity<Object> rta = cache.getIfPresent(key);
        if(rta == null){
            ResponseEntity<Object> respuesta = solicitudesHttp.get(url.toString(), Object.class);
            cache.put(key, respuesta);
            return respuesta;
        }
        return rta;
    }
}