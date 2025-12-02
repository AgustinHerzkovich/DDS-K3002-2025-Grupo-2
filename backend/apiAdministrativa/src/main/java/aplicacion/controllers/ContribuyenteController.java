package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Object> obtenerHechosContribuyente(@PathVariable(name = "id") String id) { // Para que un admin vea sus hechos
        return solicitudesHttp.get(urlBaseAgregador + "/contribuyentes/" + id + "/hechos", Object.class);
    }
}
