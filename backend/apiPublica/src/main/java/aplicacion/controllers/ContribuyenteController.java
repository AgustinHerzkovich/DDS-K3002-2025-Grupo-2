package aplicacion.controllers;

import aplicacion.config.ConfigService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import domain.peticiones.SolicitudesHttp;

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
}
