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
@RequestMapping("/apiPublica")
public class HechoController {
    private final String urlBaseAgregador;
    private final SolicitudesHttp solicitudesHttp;

    public HechoController(ConfigService configService) {
        this.urlBaseAgregador = configService.getUrl();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PostMapping("/hechos")
    public ResponseEntity<Object> reportarHecho(@RequestBody Object body) {
        return solicitudesHttp.post(urlBaseAgregador + "/hechos", body, Object.class);
    }
}