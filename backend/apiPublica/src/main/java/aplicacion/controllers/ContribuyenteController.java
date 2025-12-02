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
    private final String urlBaseDinamicas;
    private final SolicitudesHttp solicitudesHttp;

    public ContribuyenteController(ConfigService configService) {
        this.urlBaseAgregador = configService.getUrlAgregador();
        this.urlBaseDinamicas = configService.getUrlFuentesDinamicas();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PostMapping("/contribuyentes")
    public ResponseEntity<Object> agregarContribuyente(@RequestBody String body) {
        solicitudesHttp.post(urlBaseDinamicas + "/contribuyentes", body, Object.class);
        return solicitudesHttp.post(urlBaseAgregador + "/contribuyentes", body, Object.class); // Se postea tanto en fuente dinámica como en agregador
    }

    @GetMapping("/contribuyentes/{id}/hechos")
    public ResponseEntity<Object> obtenerHechosContribuyente(@PathVariable(name = "id") String id) { // Para que un usuario no admin vea sus hechos
        return solicitudesHttp.get(urlBaseDinamicas + "/contribuyentes/" + id + "/hechos", Object.class);
    }

    @GetMapping("/contribuyentes")
    public  ResponseEntity<Object> obtenerContribuyentes(@RequestParam(name = "mail", required = false) String mail) {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/contribuyentes");
        UrlHelper.appendQueryParam(url, "mail", mail);
        return solicitudesHttp.get(url.toString(), Object.class);
    }

    @PatchMapping("/contribuyentes/{id}/identidad")
    public ResponseEntity<Object> modificarIdentidadAContribuyente(@PathVariable(name = "id") String id, @RequestBody String body) {
        solicitudesHttp.patch(urlBaseDinamicas + "/contribuyentes/" + id + "/identidad", body, Object.class); // Se patchea tanto en fuente dinámica como en agregador
        return solicitudesHttp.patch(urlBaseAgregador + "/contribuyentes/" + id + "/identidad", body, Object.class);
    }
}
