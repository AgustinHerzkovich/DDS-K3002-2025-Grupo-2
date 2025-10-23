package aplicacion.controllers;

import aplicacion.config.ConfigService;
import aplicacion.helpers.UrlHelper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import domain.peticiones.SolicitudesHttp;

import java.util.List;

@RestController
@RequestMapping("/apiPublica")
public class ContribuyenteController {
    private final String urlBaseAgregador;
    private final SolicitudesHttp solicitudesHttp;

    public ContribuyenteController(ConfigService configService) {
        this.urlBaseAgregador = configService.getUrl();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PostMapping("/contribuyentes")
    public ResponseEntity<Object> agregarContribuyente(@RequestBody Object body) {
        return solicitudesHttp.post(urlBaseAgregador + "/contribuyentes", body, Object.class);
    }

    @GetMapping("/contribuyentes/{id}/hechos")
    public ResponseEntity<Object> obtenerHechosContribuyente(@PathVariable("id") Long id) {
        return solicitudesHttp.get(urlBaseAgregador + "/contribuyentes/" + id + "/hechos", Object.class);
    }

    @GetMapping("/contribuyentes")
    public  ResponseEntity<Object> obtenerContribuyentes(@RequestParam(name = "mail", required = false) String mail) {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/contribuyentes");
        UrlHelper.appendAllQueryParams(url, mail);
        return solicitudesHttp.get(url.toString(), Object.class);
    }
}
