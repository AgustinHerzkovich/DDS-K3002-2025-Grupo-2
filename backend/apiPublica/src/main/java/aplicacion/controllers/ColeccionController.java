package aplicacion.controllers;

import aplicacion.config.ConfigService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import domain.helpers.UrlHelper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/apiPublica")
public class ColeccionController {
    private ConfigService configService;
    private final String urlBaseAgregador;
    private final SolicitudesHttp solicitudesHttp;
    private final Cache<String, ResponseEntity<Object>> cache = Caffeine.newBuilder().maximumSize(100000).expireAfterWrite(1, TimeUnit.MINUTES).build();

    public ColeccionController(ConfigService configService) {
        this.configService = configService;
        this.urlBaseAgregador = configService.getUrl();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    // --- READ ---
    @GetMapping("/colecciones/{id}/hechosIrrestrictos")
    public ResponseEntity<Object> mostrarHechosIrrestrictos(
            @PathVariable("id") String id,
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
    )
    {
        // Construir la query GraphQL
        String query = """
            query ObtenerHechosDeColeccionIrrestrictos($idColeccion: String, $filtros: HechoFiltros, $page: Int, $limit: Int) {
                hechosPorColeccionIrrestrictos(idColeccion: $idColeccion, filtros: $filtros, page: $page, limit: $limit) {
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
        java.util.Map<String, Object> filtros = new java.util.HashMap<>();
        if (categoria != null) filtros.put("categoria", categoria);
        if (fechaReporteDesde != null) filtros.put("fechaReporteDesde", fechaReporteDesde);
        if (fechaReporteHasta != null) filtros.put("fechaReporteHasta", fechaReporteHasta);
        if (fechaAcontecimientoDesde != null) filtros.put("fechaAcontecimientoDesde", fechaAcontecimientoDesde);
        if (fechaAcontecimientoHasta != null) filtros.put("fechaAcontecimientoHasta", fechaAcontecimientoHasta);
        if (latitud != null) filtros.put("latitud", latitud);
        if (longitud != null) filtros.put("longitud", longitud);
        if (textoLibre != null) filtros.put("search", textoLibre);

        // Construir el mapa de variables
        java.util.Map<String, Object> variables = new java.util.HashMap<>();
        variables.put("idColeccion", id);
        variables.put("filtros", filtros.isEmpty() ? null : filtros);
        variables.put("page", page);
        variables.put("limit", size);

        // Construir el body completo de la petición GraphQL
        java.util.Map<String, Object> graphqlRequest = new java.util.HashMap<>();
        graphqlRequest.put("query", query);
        graphqlRequest.put("variables", variables);

        // Hacer la petición POST al endpoint GraphQL
        String graphqlUrl = urlBaseAgregador + "/graphql";
        return solicitudesHttp.post(graphqlUrl, graphqlRequest, Object.class);
    }

    @GetMapping("/colecciones/{id}/hechosCurados")
    public ResponseEntity<Object> mostrarHechosCurados(
            @PathVariable("id") String id,
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
        // Construir la query GraphQL
        String query = """
            query ObtenerHechosDeColeccionCurados($idColeccion: String, $filtros: HechoFiltros, $page: Int, $limit: Int) {
                hechosPorColeccionCurados(idColeccion: $idColeccion, filtros: $filtros, page: $page, limit: $limit) {
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
        java.util.Map<String, Object> filtros = new java.util.HashMap<>();
        if (categoria != null) filtros.put("categoria", categoria);
        if (fechaReporteDesde != null) filtros.put("fechaReporteDesde", fechaReporteDesde);
        if (fechaReporteHasta != null) filtros.put("fechaReporteHasta", fechaReporteHasta);
        if (fechaAcontecimientoDesde != null) filtros.put("fechaAcontecimientoDesde", fechaAcontecimientoDesde);
        if (fechaAcontecimientoHasta != null) filtros.put("fechaAcontecimientoHasta", fechaAcontecimientoHasta);
        if (latitud != null) filtros.put("latitud", latitud);
        if (longitud != null) filtros.put("longitud", longitud);
        if (textoLibre != null) filtros.put("search", textoLibre);

        // Construir el mapa de variables
        java.util.Map<String, Object> variables = new java.util.HashMap<>();
        variables.put("idColeccion", id);
        variables.put("filtros", filtros.isEmpty() ? null : filtros);
        variables.put("page", page);
        variables.put("limit", size);

        // Construir el body completo de la petición GraphQL
        java.util.Map<String, Object> graphqlRequest = new java.util.HashMap<>();
        graphqlRequest.put("query", query);
        graphqlRequest.put("variables", variables);

        // Hacer la petición POST al endpoint GraphQL
        String graphqlUrl = urlBaseAgregador + "/graphql";
        return solicitudesHttp.post(graphqlUrl, graphqlRequest, Object.class);
    }

    // --- READ ---
    @GetMapping("/colecciones")
    public ResponseEntity<Object> mostrarColecciones(@RequestParam(name = "search", required = false) String textoBuscado,
                                                     @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/colecciones");
        UrlHelper.appendQueryParam(url, "search", textoBuscado);
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "size", size);
        return solicitudesHttp.get(url.toString(), Object.class);
    }

    @GetMapping("/colecciones/{id}")
    public ResponseEntity<Object> mostrarColeccion(@PathVariable("id") String id) {
        return solicitudesHttp.get(urlBaseAgregador + "/colecciones/" + id, Object.class);
    }

    @GetMapping("/colecciones/index")
    public ResponseEntity<Object> obtenerRecomendaciones(@RequestParam(name = "search", required = true) String texto,
                                                         @RequestParam(name="limit", required = false, defaultValue = "5") Integer limite) {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/colecciones/index");
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