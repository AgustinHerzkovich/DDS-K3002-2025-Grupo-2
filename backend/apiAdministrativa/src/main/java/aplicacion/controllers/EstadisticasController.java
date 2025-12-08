package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.peticiones.ResponseWrapper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apiAdministrativa")
public class EstadisticasController {
    private final ConfigService configService;
    private final SolicitudesHttp solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());

    public EstadisticasController(@Lazy ConfigService configService) {
        this.configService = configService;
    }

    @PostMapping("/estadisticas/actualizar")
    public ResponseEntity<?> actualizarEstadisticas() {
        return ResponseWrapper.wrapResponse(solicitudesHttp.post(configService.getUrlEstadisticas() + "/actualizar", null, String.class));
    }

}
