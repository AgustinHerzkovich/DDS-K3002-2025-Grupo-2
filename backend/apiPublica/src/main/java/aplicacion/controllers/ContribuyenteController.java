package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.helpers.UrlHelper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import domain.peticiones.SolicitudesHttp;

@RestController
@RequestMapping("/apiPublica")
public class ContribuyenteController {
    private final String urlBaseAgregador;
    private final SolicitudesHttp solicitudesHttp;

    public ContribuyenteController(ConfigService configService) {
        this.urlBaseAgregador = configService.getUrlAgregador();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PostMapping("/contribuyentes")
    public ResponseEntity<Object> agregarContribuyente(@RequestBody Object body) {
        return solicitudesHttp.post(urlBaseAgregador + "/contribuyentes", body, Object.class);
    }

    @GetMapping("/contribuyentes/{id}/hechos")
    public ResponseEntity<Object> obtenerHechosContribuyente(@PathVariable(name = "id") Long id) {
        return solicitudesHttp.get(urlBaseAgregador + "/contribuyentes/" + id + "/hechos", Object.class);
    }

    @GetMapping("/contribuyentes")
    public  ResponseEntity<Object> obtenerContribuyentes(@RequestParam(name = "mail", required = false) String mail) {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/contribuyentes");
        UrlHelper.appendQueryParam(url, "mail", mail);
        return solicitudesHttp.get(url.toString(), Object.class);
    }
    @PatchMapping("/contribuyentes/{id}/identidad")
    public ResponseEntity<Object> modificarIdentidadAContribuyente(@RequestBody Object body , @PathVariable(name = "id") Long id) {
        return solicitudesHttp.patch(urlBaseAgregador + "/contribuyentes/" + id + "/identidad", body, Object.class);
    }
}
