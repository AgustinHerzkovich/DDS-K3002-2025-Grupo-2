package aplicacion.controllers;

import aplicacion.config.ConfigService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import domain.helpers.UrlHelper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
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

    @GetMapping("/hechos")
    public ResponseEntity<Object> obtenerHechos(
            @RequestParam(name = "categoria", required = false) String categoria,
            @RequestParam(name = "fechaReporteDesde", required = false) String fechaReporteDesde,
            @RequestParam(name = "fechaReporteHasta", required = false) String fechaReporteHasta,
            @RequestParam(name = "fechaAcontecimientoDesde", required = false) String fechaAcontecimientoDesde,
            @RequestParam(name = "fechaAcontecimientoHasta", required = false) String fechaAcontecimientoHasta,
            @RequestParam(name = "latitud", required = false) Double latitud,
            @RequestParam(name = "longitud", required = false) Double longitud,
            @RequestParam(name = "radio", required = false) Double radio,
            @RequestParam(name = "search", required = false) String textoLibre,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "100") Integer size
    ) {
        // Construir la query GraphQL
        String query = """
            query ObtenerHechos($filtros: HechoFiltros, $page: Int, $limit: Int) {
                hechosEnMapa(filtros: $filtros, page: $page, limit: $limit) {
                    content {
                        id
                        titulo
                        latitud
                        longitud
                        categoria
                        fechaCarga
                    }
                    pageInfo {
                        totalElements
                        totalPages
                        number
                        size
                        hasNext
                        hasPrevious
                    }
                }
            }
            """;

        // Construir el mapa de filtros
        Map<String, Object> filtros = new HashMap<>();
        if (categoria != null) filtros.put("categoria", categoria);
        // Convertir las fechas a formato ISO-8601 con zona horaria
        if (fechaReporteDesde != null) {
            String fecha = LocalDateTime.parse(URLDecoder.decode(fechaReporteDesde, StandardCharsets.UTF_8))
                    .atOffset(ZoneOffset.UTC)
                    .toString();
            filtros.put("fechaReporteDesde", fecha);
        }
        if (fechaReporteHasta != null) {
            String fecha = LocalDateTime.parse(URLDecoder.decode(fechaReporteHasta, StandardCharsets.UTF_8))
                    .atOffset(ZoneOffset.UTC)
                    .toString();
            filtros.put("fechaReporteHasta", fecha);
        }
        if (fechaAcontecimientoDesde != null) {
            String fecha = LocalDateTime.parse(URLDecoder.decode(fechaAcontecimientoDesde, StandardCharsets.UTF_8))
                    .atOffset(ZoneOffset.UTC)
                    .toString();
            filtros.put("fechaAcontecimientoDesde", fecha);
        }
        if (fechaAcontecimientoHasta != null) {
            String fecha = LocalDateTime.parse(URLDecoder.decode(fechaAcontecimientoHasta, StandardCharsets.UTF_8))
                    .atOffset(ZoneOffset.UTC)
                    .toString();
            filtros.put("fechaAcontecimientoHasta", fecha);
        }

        if (latitud != null) filtros.put("latitud", latitud);
        if (longitud != null) filtros.put("longitud", longitud);
        if (radio != null) filtros.put("radio", radio);
        if (textoLibre != null) filtros.put("search", textoLibre);

        // Construir el mapa de variables
        Map<String, Object> variables = new HashMap<>();
        variables.put("filtros", filtros.isEmpty() ? null : filtros);
        variables.put("page", page);
        variables.put("limit", size);

        // Construir el body completo de la petición GraphQL
        Map<String, Object> graphqlRequest = new HashMap<>();
        graphqlRequest.put("query", query);
        graphqlRequest.put("variables", variables);

        // Hacer la petición POST al endpoint GraphQL
        String graphqlUrl = urlBaseAgregador + "/graphql";
        return solicitudesHttp.post(graphqlUrl, graphqlRequest, Object.class);
    }

    @GetMapping("/hechos/{id}")
    public ResponseEntity<Object> obtenerHechoPorId(@PathVariable(name = "id") String id) {
        String url = urlBaseAgregador + "/hechos/" + id;
        return solicitudesHttp.get(url, Object.class);
    }

    @GetMapping("/hechos/index")
    public ResponseEntity<Object> obtenerRecomendaciones(@RequestParam(name="search", required = true) String texto,
                                                         @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limite) {
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
    /*
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
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "100") Integer size
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
    }*/
}