package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.helpers.UrlHelper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiAdministrativa")
public class SolicitudController {
    private final String urlBaseAgregador;
    private final SolicitudesHttp solicitudesHttp;

    public SolicitudController(ConfigService configService) {
        this.urlBaseAgregador = configService.getUrlAgregador();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PatchMapping("/solicitudes/{id}/estado")
    public ResponseEntity<Object> actualizarEstadoSolicitud(
            @PathVariable(name = "id") Long id,
            @RequestBody String revisionSolicitud) {
        return solicitudesHttp.patch(urlBaseAgregador + "/solicitudes/" + id + "/estado", revisionSolicitud, Object.class);
    }

    @GetMapping("/solicitudes")
    public ResponseEntity<Object> obtenerSolicitudes(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(name = "size", defaultValue = "3") Integer size) {
        StringBuilder url = new StringBuilder(urlBaseAgregador + "/solicitudes");
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "size", size);

        return solicitudesHttp.get(url.toString(), Object.class);
    }

    @GetMapping("/solicitudes/{id}")
    public ResponseEntity<Object> obtenerSolicitud(@PathVariable(name = "id") Long id) {
        return solicitudesHttp.get(urlBaseAgregador + "/solicitudes/" + id, Object.class);
    }
}