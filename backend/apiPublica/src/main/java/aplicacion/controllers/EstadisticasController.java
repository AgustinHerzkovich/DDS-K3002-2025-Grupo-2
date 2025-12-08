package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.helpers.UrlHelper;
import domain.peticiones.ResponseWrapper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiPublica")
public class EstadisticasController {
    private final SolicitudesHttp solicitudesHttp;
    private final ConfigService configService;

    public EstadisticasController(@Lazy ConfigService configService) {
        this.configService = configService;
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @GetMapping("/provinciasConMasHechosDeColeccion")
    public ResponseEntity<?> provinciasDeColeccion(@RequestParam(name = "idColeccion", required = false) String idColeccion,
                                                   @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(name = "limit", defaultValue = "1") Integer limit,
                                                   @RequestHeader(name = HttpHeaders.ACCEPT, defaultValue = "application/json") String accept) {
        StringBuilder url = new StringBuilder(configService.getUrlEstadisticas() + "/provinciasConMasHechosDeColeccion");
        UrlHelper.appendQueryParamSinEncode(url, "idColeccion", idColeccion);
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "limit", limit);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, accept);

        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), headers, String.class));
    }

    @GetMapping("/categoriasConMasHechos")
    public ResponseEntity<?> categoriaConMasHechosReportados(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                            @RequestParam(name = "limit", defaultValue = "1") Integer limit,
                                                            @RequestHeader(name = HttpHeaders.ACCEPT, defaultValue = "application/json") String accept) {
        StringBuilder url = new StringBuilder(configService.getUrlEstadisticas() + "/categoriasConMasHechos");
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "limit", limit);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, accept);

        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), headers, String.class));
    }

    @GetMapping("/provinciasConMasHechosDeCategoria")
    public ResponseEntity<?> provinciaConMasHechosDeCategoria(@RequestParam(name = "nombreCategoria", required = false) String nombreCategoria,
                                                              @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                              @RequestParam(name = "limit", defaultValue = "1") Integer limit,
                                                              @RequestHeader(name = HttpHeaders.ACCEPT, defaultValue = "application/json") String accept) {
        StringBuilder url = new StringBuilder(configService.getUrlEstadisticas() + "/provinciasConMasHechosDeCategoria");
        UrlHelper.appendQueryParamSinEncode(url, "nombreCategoria", nombreCategoria);
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "limit", limit);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, accept);

        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), headers, String.class));
    }

    @GetMapping("/horaConMasHechosDeCategoria")
    public ResponseEntity<?> horaConMasHechosDeCategoria(@RequestParam(name = "nombreCategoria", required = false) String nombreCategoria,
                                                         @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                         @RequestParam(name = "limit", defaultValue = "1") Integer limit,
                                                         @RequestHeader(name = HttpHeaders.ACCEPT, defaultValue = "application/json") String accept) {
        StringBuilder url = new StringBuilder(configService.getUrlEstadisticas() + "/horaConMasHechosDeCategoria");
        UrlHelper.appendQueryParamSinEncode(url, "nombreCategoria", nombreCategoria);
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "limit", limit);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, accept);

        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), headers, String.class));
    }

    @GetMapping("/solicitudesDeEliminacionSpam")
    public ResponseEntity<?> solicitudesSpam(@RequestHeader(name = HttpHeaders.ACCEPT, defaultValue = "application/json") String accept) {
        String url = configService.getUrlEstadisticas() + "/solicitudesDeEliminacionSpam";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, accept);

        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url, headers, String.class));
    }


    @GetMapping("/estadisticas/categorias")
    public ResponseEntity<?> categoriasDisponibles(@RequestParam(name = "search", required = false) String search,
                                                              @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                              @RequestParam(name = "limit", defaultValue = "10") Integer limit) {
        StringBuilder url = new StringBuilder(configService.getUrlEstadisticas() + "/categoriasDisponibles");
        UrlHelper.appendQueryParamSinEncode(url, "search", search);
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "limit", limit);

        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), String.class));
    }


    @GetMapping("/estadisticas/colecciones")
    public ResponseEntity<?> coleccionesDisponibles(@RequestParam(name = "search", required = false) String search,
                                                              @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                              @RequestParam(name = "limit", defaultValue = "10") Integer limit) {
        StringBuilder url = new StringBuilder(configService.getUrlEstadisticas() + "/coleccionesDisponibles");
        UrlHelper.appendQueryParamSinEncode(url, "search", search);
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "limit", limit);

        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), String.class));
    }
}
