package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.peticiones.ResponseWrapper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/apiAdministrativa")
public class FuenteEstaticaController {
    private final ConfigService configService;
    private final SolicitudesHttp solicitudesHttp;

    public FuenteEstaticaController(@Lazy ConfigService configService) {
        this.configService = configService;
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PostMapping("/archivos/por-url")
    public ResponseEntity<?> subirArchivoPorUrl(@RequestBody String body) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.post(configService.getUrlFuentesEstaticas() + "/archivos/por-url", body, String.class));
    }

    @PostMapping("/archivos")
    public ResponseEntity<?> subirArchivos(@RequestParam("files") MultipartFile[] files) {
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        for (MultipartFile file : files) {
            body.add("files", file.getResource());
        }

        return ResponseWrapper.wrapResponse(solicitudesHttp.postMultipart(configService.getUrlFuentesEstaticas() + "/archivos", body, String.class));
    }
}
