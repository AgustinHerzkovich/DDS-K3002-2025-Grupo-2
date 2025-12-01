package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.helpers.UrlHelper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiPublica")
public class EstadisticasController {
    private final String urlBaseEstadisticas;
    private final SolicitudesHttp solicitudesHttp;

    public EstadisticasController(ConfigService configService) {
        this.urlBaseEstadisticas = configService.getUrlEstadisticas();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @GetMapping("/provinciasConMasHechosDeColeccion")
    public ResponseEntity<?> provinciasDeColeccion(@RequestParam(name = "idColeccion", required = false) String idColeccion,
                                                   @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(name = "limit", defaultValue = "1") Integer limit,
                                                   @RequestHeader(name = HttpHeaders.ACCEPT, defaultValue = "application/json") String accept) {
        StringBuilder url = new StringBuilder(urlBaseEstadisticas + "/provinciasConMasHechosDeColeccion");
        UrlHelper.appendQueryParam(url, "idColeccion", idColeccion);
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "limit", limit);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, accept);

        return solicitudesHttp.get(url.toString(), headers, Object.class);
    }

    @GetMapping("/categoriasConMasHechos")
    public ResponseEntity<?> categoriaConMasHechosReportados(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                            @RequestParam(name = "limit", defaultValue = "1") Integer limit,
                                                            @RequestHeader(name = HttpHeaders.ACCEPT, defaultValue = "application/json") String accept) {
        StringBuilder url = new StringBuilder(urlBaseEstadisticas + "/categoriasConMasHechos");
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "limit", limit);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, accept);

        return solicitudesHttp.get(url.toString(), headers, Object.class);
    }

    @GetMapping("/provinciasConMasHechosDeCategoria")
    public ResponseEntity<?> provinciaConMasHechosDeCategoria(@RequestParam(name = "nombreCategoria", required = false) String nombreCategoria,
                                                              @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                              @RequestParam(name = "limit", defaultValue = "1") Integer limit,
                                                              @RequestHeader(name = HttpHeaders.ACCEPT, defaultValue = "application/json") String accept) {
        StringBuilder url = new StringBuilder(urlBaseEstadisticas + "/provinciasConMasHechosDeCategoria");
        UrlHelper.appendQueryParam(url, "nombreCategoria", nombreCategoria);
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "limit", limit);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, accept);

        return solicitudesHttp.get(url.toString(), headers, Object.class);
    }

    @GetMapping("/horaConMasHechosDeCategoria")
    public ResponseEntity<?> horaConMasHechosDeCategoria(@RequestParam(name = "nombreCategoria", required = false) String nombreCategoria,
                                                         @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                         @RequestParam(name = "limit", defaultValue = "1") Integer limit,
                                                         @RequestHeader(name = HttpHeaders.ACCEPT, defaultValue = "application/json") String accept) {
        StringBuilder url = new StringBuilder(urlBaseEstadisticas + "/horaConMasHechosDeCategoria");
        UrlHelper.appendQueryParam(url, "nombreCategoria", nombreCategoria);
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "limit", limit);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, accept);

        return solicitudesHttp.get(url.toString(), headers, Object.class);
    }

    @GetMapping("/solicitudesDeEliminacionSpam")
    public ResponseEntity<?> solicitudesSpam(@RequestHeader(name = HttpHeaders.ACCEPT, defaultValue = "application/json") String accept) {
        StringBuilder url = new StringBuilder(urlBaseEstadisticas + "/solicitudesDeEliminacionSpam");

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, accept);

        return solicitudesHttp.get(url.toString(), headers, Object.class);
    }
}
