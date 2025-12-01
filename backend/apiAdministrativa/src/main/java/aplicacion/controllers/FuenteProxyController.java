package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apiAdministrativa")
public class FuenteProxyController {
    private final String urlBaseProxy;
    private final SolicitudesHttp solicitudesHttp;

    public FuenteProxyController(ConfigService configService) {
        this.urlBaseProxy = configService.getUrlFuentesProxy();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PostMapping("/fuentesProxy")
    public ResponseEntity<String> guardarFuente(@RequestBody String body){
        return solicitudesHttp.post(urlBaseProxy + "/fuentesProxy", body, String.class);
    }
}
