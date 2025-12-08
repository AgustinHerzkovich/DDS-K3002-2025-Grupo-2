package aplicacion.controllers;

import aplicacion.config.ConfigService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import domain.helpers.UrlHelper;
import domain.peticiones.ResponseWrapper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
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
public class ColeccionController {

    private final SolicitudesHttp solicitudesHttp;
    private final Cache<String, ResponseEntity<?>> cache = Caffeine.newBuilder().maximumSize(100000).expireAfterWrite(1, TimeUnit.MINUTES).build();
    private final ConfigService configService;

    public ColeccionController(@Lazy ConfigService configService) {
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
        this.configService = configService;
    }

    // --- READ ---
    @GetMapping("/colecciones/{id}/hechosIrrestrictos")
    public ResponseEntity<?> mostrarHechosIrrestrictos(
            @PathVariable(name = "id") String id,
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
            @RequestParam(name = "size", defaultValue = "100") Integer size,
            // Parámetros para seleccionar campos del Hecho
            @RequestParam(name = "includeId", defaultValue = "true") Boolean includeId,
            @RequestParam(name = "includeTitulo", defaultValue = "true") Boolean includeTitulo,
            @RequestParam(name = "includeDescripcion", defaultValue = "false") Boolean includeDescripcion,
            @RequestParam(name = "includeLatitud", defaultValue = "true") Boolean includeLatitud,
            @RequestParam(name = "includeLongitud", defaultValue = "true") Boolean includeLongitud,
            @RequestParam(name = "includeCategoria", defaultValue = "true") Boolean includeCategoria,
            @RequestParam(name = "includeFechaCarga", defaultValue = "true") Boolean includeFechaCarga,
            @RequestParam(name = "includeFechaAcontecimiento", defaultValue = "false") Boolean includeFechaAcontecimiento,
            @RequestParam(name = "includeFechaUltimaModificacion", defaultValue = "false") Boolean includeFechaUltimaModificacion,
            @RequestParam(name = "includeContenidoTexto", defaultValue = "false") Boolean includeContenidoTexto,
            @RequestParam(name = "includeAnonimato", defaultValue = "false") Boolean includeAnonimato
    )
    {
        // Determinar los campos a solicitar
        String camposHecho = obtenerCamposHecho(
                includeId, includeTitulo, includeDescripcion, includeLatitud, includeLongitud,
                includeCategoria, includeFechaCarga, includeFechaAcontecimiento,
                includeFechaUltimaModificacion, includeContenidoTexto, includeAnonimato
        );

        // Construir la query GraphQL
        String query = """
            query ObtenerHechosDeColeccionIrrestrictos($idColeccion: String, $curados: Boolean, $filtros: HechoFiltros, $page: Int, $limit: Int) {
                getHechosPorColeccion(idColeccion: $idColeccion, curados: $curados, filtros: $filtros, page: $page, limit: $limit) {
                    content {
                        %s
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
            """.formatted(camposHecho);

        // Construir el mapa de filtros
        Map<String, Object> filtros = new HashMap<>();
        if (categoria != null) filtros.put("categoria", categoria);
        // Convertir las fechas a formato ISO-8601 con zona horaria
        if (fechaReporteDesde != null && !fechaReporteDesde.isEmpty()) {
            String fecha = LocalDateTime.parse(URLDecoder.decode(fechaReporteDesde, StandardCharsets.UTF_8))
                    .atOffset(ZoneOffset.UTC)
                    .toString();
            filtros.put("fechaReporteDesde", fecha);
        }
        if (fechaReporteHasta != null && !fechaReporteHasta.isEmpty()) {
            String fecha = LocalDateTime.parse(URLDecoder.decode(fechaReporteHasta, StandardCharsets.UTF_8))
                    .atOffset(ZoneOffset.UTC)
                    .toString();
            filtros.put("fechaReporteHasta", fecha);
        }
        if (fechaAcontecimientoDesde != null && !fechaAcontecimientoDesde.isEmpty() ) {
            String fecha = LocalDateTime.parse(URLDecoder.decode(fechaAcontecimientoDesde, StandardCharsets.UTF_8))
                    .atOffset(ZoneOffset.UTC)
                    .toString();
            filtros.put("fechaAcontecimientoDesde", fecha);
        }
        if (fechaAcontecimientoHasta != null && !fechaAcontecimientoHasta.isEmpty()) {
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
        variables.put("idColeccion", id);
        variables.put("curados", false);
        variables.put("filtros", filtros.isEmpty() ? null : filtros);
        variables.put("page", page);
        variables.put("limit", size);

        // Construir el body completo de la petición GraphQL
        Map<String, Object> graphqlRequest = new HashMap<>();
        graphqlRequest.put("query", query);
        graphqlRequest.put("variables", variables);

        // Hacer la petición POST al endpoint GraphQL
        String graphqlUrl = configService.getUrlAgregador() + "/graphql";
        return ResponseWrapper.wrapResponse(solicitudesHttp.post(graphqlUrl, graphqlRequest, String.class));
    }

    @GetMapping("/colecciones/{id}/hechosCurados")
    public ResponseEntity<?> mostrarHechosCurados(
            @PathVariable(name = "id") String id,
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
            @RequestParam(name = "size", defaultValue = "100") Integer size,
            // Parámetros para seleccionar campos del Hecho
            @RequestParam(name = "includeId", defaultValue = "true") Boolean includeId,
            @RequestParam(name = "includeTitulo", defaultValue = "true") Boolean includeTitulo,
            @RequestParam(name = "includeDescripcion", defaultValue = "false") Boolean includeDescripcion,
            @RequestParam(name = "includeLatitud", defaultValue = "true") Boolean includeLatitud,
            @RequestParam(name = "includeLongitud", defaultValue = "true") Boolean includeLongitud,
            @RequestParam(name = "includeCategoria", defaultValue = "true") Boolean includeCategoria,
            @RequestParam(name = "includeFechaCarga", defaultValue = "true") Boolean includeFechaCarga,
            @RequestParam(name = "includeFechaAcontecimiento", defaultValue = "false") Boolean includeFechaAcontecimiento,
            @RequestParam(name = "includeFechaUltimaModificacion", defaultValue = "false") Boolean includeFechaUltimaModificacion,
            @RequestParam(name = "includeContenidoTexto", defaultValue = "false") Boolean includeContenidoTexto,
            @RequestParam(name = "includeAnonimato", defaultValue = "false") Boolean includeAnonimato
    ) {
        // Determinar los campos a solicitar
        String camposHecho = obtenerCamposHecho(
                includeId, includeTitulo, includeDescripcion, includeLatitud, includeLongitud,
                includeCategoria, includeFechaCarga, includeFechaAcontecimiento,
                includeFechaUltimaModificacion, includeContenidoTexto, includeAnonimato
        );

        // Construir la query GraphQL
        String query = """
            query ObtenerHechosDeColeccionCurados($idColeccion: String, $curados: Boolean, $filtros: HechoFiltros, $page: Int, $limit: Int) {
                getHechosPorColeccion(idColeccion: $idColeccion, curados: $curados, filtros: $filtros, page: $page, limit: $limit) {
                    content {
                        %s
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
            """.formatted(camposHecho);

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
        variables.put("idColeccion", id);
        variables.put("curados", true);
        variables.put("filtros", filtros.isEmpty() ? null : filtros);
        variables.put("page", page);
        variables.put("limit", size);

        // Construir el body completo de la petición GraphQL
        Map<String, Object> graphqlRequest = new HashMap<>();
        graphqlRequest.put("query", query);
        graphqlRequest.put("variables", variables);

        // Hacer la petición POST al endpoint GraphQL
        String graphqlUrl = configService.getUrlAgregador() + "/graphql";
        return ResponseWrapper.wrapResponse(solicitudesHttp.post(graphqlUrl, graphqlRequest, String.class));
    }

    // --- READ ---
    @GetMapping("/colecciones")
    public ResponseEntity<?> mostrarColecciones(@RequestParam(name = "search", required = false) String textoBuscado,
                                                     @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        StringBuilder url = new StringBuilder(configService.getUrlAgregador() + "/colecciones");
        UrlHelper.appendQueryParam(url, "search", textoBuscado);
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "size", size);
        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), String.class));
    }

    @GetMapping("/colecciones/{id}")
    public ResponseEntity<?> mostrarColeccion(@PathVariable(name = "id") String id) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.get(configService.getUrlAgregador() + "/colecciones/" + id, String.class));
    }

    @GetMapping("/colecciones/index")
    public ResponseEntity<?> obtenerRecomendaciones(@RequestParam(name = "search", required = true) String texto,
                                                         @RequestParam(name="limit", required = false, defaultValue = "5") Integer limite) {
        StringBuilder url = new StringBuilder(configService.getUrlAgregador() + "/colecciones/index");
        UrlHelper.appendQueryParam(url, "search", texto);
        UrlHelper.appendQueryParam(url, "limit", limite);
        String key = texto + "|" + limite;
        ResponseEntity<?> rta = cache.getIfPresent(key);
        if(rta == null){
            ResponseEntity<?> respuesta = ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), String.class));
            cache.put(key, respuesta);
            return respuesta;
        }
        return rta;
    }

    private String obtenerCamposHecho(
            Boolean includeId, Boolean includeTitulo, Boolean includeDescripcion,
            Boolean includeLatitud, Boolean includeLongitud, Boolean includeCategoria,
            Boolean includeFechaCarga, Boolean includeFechaAcontecimiento,
            Boolean includeFechaUltimaModificacion, Boolean includeContenidoTexto,
            Boolean includeAnonimato
    ) {
        StringBuilder camposBuilder = new StringBuilder();

        if (includeId) agregarCampo(camposBuilder, "id");
        if (includeTitulo) agregarCampo(camposBuilder, "titulo");
        if (includeDescripcion) agregarCampo(camposBuilder, "descripcion");
        if (includeLatitud) agregarCampo(camposBuilder, "latitud");
        if (includeLongitud) agregarCampo(camposBuilder, "longitud");
        if (includeCategoria) agregarCampo(camposBuilder, "categoria");
        if (includeFechaCarga) agregarCampo(camposBuilder, "fechaCarga");
        if (includeFechaAcontecimiento) agregarCampo(camposBuilder, "fechaAcontecimiento");
        if (includeFechaUltimaModificacion) agregarCampo(camposBuilder, "fechaUltimaModificacion");
        if (includeContenidoTexto) agregarCampo(camposBuilder, "contenidoTexto");
        if (includeAnonimato) agregarCampo(camposBuilder, "anonimato");

        // Si no se seleccionó ningún campo, devolver al menos el id
        if (camposBuilder.isEmpty()) {
            return "id";
        }

        return camposBuilder.toString();
    }

    private void agregarCampo(StringBuilder builder, String campo) {
        if (!builder.isEmpty()) {
            builder.append("\n                        ");
        }
        builder.append(campo);
    }
}