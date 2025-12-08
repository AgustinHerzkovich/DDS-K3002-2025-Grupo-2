package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.helpers.UrlHelper;
import domain.peticiones.ResponseWrapper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiAdministrativa")
public class SolicitudController {
    private final ConfigService configService;
    private final SolicitudesHttp solicitudesHttp;

    public SolicitudController(@Lazy ConfigService configService) {
        this.configService = configService;
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PatchMapping("/solicitudes/{id}/estado")
    public ResponseEntity<?> actualizarEstadoSolicitud(
            @PathVariable(name = "id") Long id,
            @RequestBody String revisionSolicitud) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.patch(configService.getUrlAgregador() + "/solicitudes/" + id + "/estado", revisionSolicitud, String.class));
    }

    @GetMapping("/solicitudes")
    public ResponseEntity<?> obtenerSolicitudes(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(name = "size", defaultValue = "3") Integer size) {
        StringBuilder url = new StringBuilder(configService.getUrlAgregador() + "/solicitudes");
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "size", size);

        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), String.class));
    }

    @GetMapping("/solicitudes/{id}")
    public ResponseEntity<?> obtenerSolicitud(@PathVariable(name = "id") Long id) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.get(configService.getUrlAgregador() + "/solicitudes/" + id, String.class));
    }
}