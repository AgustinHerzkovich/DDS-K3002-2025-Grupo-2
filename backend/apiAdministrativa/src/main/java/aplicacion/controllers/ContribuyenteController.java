package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.helpers.UrlHelper;
import domain.peticiones.ResponseWrapper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiAdministrativa")
public class ContribuyenteController {
    private final String urlBaseAgregador;
    private final SolicitudesHttp solicitudesHttp;

    public ContribuyenteController(ConfigService configService) {
        this.urlBaseAgregador = configService.getUrlAgregador();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @GetMapping("/contribuyentes/{id}/hechos")
    public ResponseEntity<?> obtenerHechosContribuyente(@PathVariable(name = "id") String id,
                                                        @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                        @RequestParam(name = "size", defaultValue = "20") Integer size) { // Para que un admin vea sus hechos
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/contribuyentes/" + id + "/hechos");
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "size", size);
        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), String.class));
    }
}
