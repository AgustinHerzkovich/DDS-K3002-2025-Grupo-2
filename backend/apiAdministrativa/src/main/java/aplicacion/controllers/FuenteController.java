package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.helpers.UrlHelper;
import domain.peticiones.ResponseWrapper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apiAdministrativa")
public class FuenteController {
    private final String urlBaseAgregador;
    private final SolicitudesHttp solicitudesHttp;

    public FuenteController(ConfigService configService) {
        this.urlBaseAgregador = configService.getUrlAgregador();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @GetMapping("/fuentes")
    public ResponseEntity<?> obtenerFuentes(@RequestParam(value = "tipo", required = false) String tipoFuente,
                                            @RequestParam(name = "page", defaultValue = "0") Integer page,
                                            @RequestParam(name = "limit", defaultValue = "10") Integer limit){
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/fuentes");
        UrlHelper.appendQueryParam(url, "tipo", tipoFuente);
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "limit", limit);
        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), String.class));
    }
}
