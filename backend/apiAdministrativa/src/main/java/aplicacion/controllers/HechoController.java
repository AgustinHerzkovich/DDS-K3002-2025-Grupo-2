package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiAdministrativa")
public class HechoController {
    private final ConfigService configService;
    private final String urlBaseAgregador;
    private final SolicitudesHttp solicitudesHttp;

    public HechoController(ConfigService configService) {
        this.configService = configService;
        this.urlBaseAgregador = configService.getUrl();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PostMapping("/hechos/{id}/tags")
    public ResponseEntity<Object> agregarEtiqueta(@PathVariable(name="id") String id, @RequestBody String body) {
        return solicitudesHttp.post(urlBaseAgregador + "/hechos/" + id + "/tags", body, Object.class);
    }

    @DeleteMapping("/hechos/{id}/tags/{nombreTag}")
    public ResponseEntity<Void> quitarEtiqueta(@PathVariable("id") String id,
                                             @PathVariable(name="nombreTag") String nombreTag) {
        return solicitudesHttp.delete(urlBaseAgregador + "/hechos/" + id + "/tags/" + nombreTag, Void.class);
    }

    @PostMapping("/hechos")
    public ResponseEntity<Object> reportarHecho(@RequestBody Object body) {
        return solicitudesHttp.post(urlBaseAgregador + "/hechos", body, Object.class);
    }
}


