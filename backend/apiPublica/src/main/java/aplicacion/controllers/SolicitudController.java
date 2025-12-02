package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiPublica")
@CrossOrigin(origins = "http://localhost:8094", allowCredentials = "true")
public class SolicitudController {
    private final String urlBaseAgregador;
    private final SolicitudesHttp solicitudesHttp;

    public SolicitudController(ConfigService configService) {
        this.urlBaseAgregador = configService.getUrlAgregador();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PostMapping("/solicitudes")
    public ResponseEntity<Object> crearSolicitud(@RequestBody String body) {
        return solicitudesHttp.post(urlBaseAgregador + "/solicitudes", body, Object.class);
    }
}