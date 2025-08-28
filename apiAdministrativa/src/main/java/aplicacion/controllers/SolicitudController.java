package aplicacion.controllers;

import aplicacion.config.ConfigService;
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
        this.urlBaseAgregador = configService.getUrl();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PatchMapping("/solicitudes/{id}/estado")
    public ResponseEntity<Object> actualizarEstadoSolicitud(
            @PathVariable Long id,
            @RequestBody String nuevoEstado) {
        solicitudesHttp.patch(urlBaseAgregador + "/solicitudes/" + id + "/estado", nuevoEstado);
        return ResponseEntity.ok().build();
    }
}