package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.helpers.UrlHelper;
import domain.peticiones.ResponseWrapper;
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
    public ResponseEntity<?> agregarContribuyente(@RequestBody String body) {
        solicitudesHttp.post(urlBaseDinamicas + "/contribuyentes", body, String.class);
        return ResponseWrapper.wrapResponse(solicitudesHttp.post(urlBaseAgregador + "/contribuyentes", body, String.class)); // Se postea tanto en fuente dinámica como en agregador
    }

    @GetMapping("/contribuyentes/{id}/hechos")
    public ResponseEntity<?> obtenerHechosContribuyente(@PathVariable(name = "id") String id,
                                                        @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                        @RequestParam(name = "size", defaultValue = "20") Integer size) { // Para que un usuario no admin vea sus hechos
        StringBuilder url = new StringBuilder(urlBaseDinamicas + "/contribuyentes/" + id + "/hechos");
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "size", size);
        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), String.class));
    }

    @GetMapping("/contribuyentes")
    public  ResponseEntity<?> obtenerContribuyentes(@RequestParam(name = "mail", required = false) String mail) {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/contribuyentes");
        UrlHelper.appendQueryParam(url, "mail", mail);
        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), String.class));
    }

    @PatchMapping("/contribuyentes/{id}/identidad")
    public ResponseEntity<?> modificarIdentidadAContribuyente(@PathVariable(name = "id") String id, @RequestBody String body) {
        solicitudesHttp.patch(urlBaseDinamicas + "/contribuyentes/" + id + "/identidad", body, String.class); // Se patchea tanto en fuente dinámica como en agregador
        return ResponseWrapper.wrapResponse(solicitudesHttp.patch(urlBaseAgregador + "/contribuyentes/" + id + "/identidad", body, String.class));
    }
}
