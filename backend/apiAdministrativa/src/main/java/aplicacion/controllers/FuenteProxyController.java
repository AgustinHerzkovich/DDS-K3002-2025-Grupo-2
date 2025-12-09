package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.peticiones.ResponseWrapper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiAdministrativa")
public class FuenteProxyController {
    private final ConfigService configService;

    private final SolicitudesHttp solicitudesHttp;

    public FuenteProxyController(@Lazy ConfigService configService) {
        this.configService = configService;
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PostMapping("/fuentesProxy")
    public ResponseEntity<?> guardarFuente(@RequestBody String body,
                                           @RequestParam(name = "tipo") String tipoFuente) {
        String path;
        if (tipoFuente.equals("demo")) {
            path = "/fuentesDemo";
        } else if (tipoFuente.equals("metamapa")) {
            path = "/fuentesMetamapa";
        } else {
            return ResponseEntity.badRequest().body("Tipo de fuente no válido");
        }
        return ResponseWrapper.wrapResponse(solicitudesHttp.post(configService.getUrlFuentesProxy() + path, body, String.class));
    }

    @GetMapping("/agregadores")
    public ResponseEntity<?> obtenerAgregadores() {
        return ResponseWrapper.wrapResponse(solicitudesHttp.get(configService.getUrlAgregador() + "/agregadores", String.class));
    }
}
