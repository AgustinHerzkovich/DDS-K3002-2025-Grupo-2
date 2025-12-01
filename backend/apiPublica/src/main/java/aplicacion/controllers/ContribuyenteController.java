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
    public ResponseEntity<String> agregarContribuyente(@RequestBody String body) {
        return solicitudesHttp.post(urlBaseAgregador + "/contribuyentes", body, String.class);
    }

    @GetMapping("/contribuyentes/{id}/hechos")
    public ResponseEntity<String> obtenerHechosContribuyente(@PathVariable(name = "id") String id) {
        return solicitudesHttp.get(urlBaseAgregador + "/contribuyentes/" + id + "/hechos", String.class);
    }

    @GetMapping("/contribuyentes")
    public  ResponseEntity<String> obtenerContribuyentes(@RequestParam(name = "mail", required = false) String mail) {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/contribuyentes");
        UrlHelper.appendQueryParam(url, "mail", mail);
        return solicitudesHttp.get(url.toString(), String.class);
    }
    @PatchMapping("/contribuyentes/{id}/identidad")
    public ResponseEntity<String> modificarIdentidadAContribuyente(@RequestBody String body , @PathVariable(name = "id") String id) {
        return solicitudesHttp.patch(urlBaseAgregador + "/contribuyentes/" + id + "/identidad", body, String.class);
    }
}
