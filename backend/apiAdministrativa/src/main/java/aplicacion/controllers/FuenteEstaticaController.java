package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/apiAdministrativa")
public class FuenteEstaticaController {
    private final String urlBaseEstaticas;
    private final SolicitudesHttp solicitudesHttp;

    public FuenteEstaticaController(ConfigService configService) {
        this.urlBaseEstaticas = configService.getUrlFuentesEstaticas();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PostMapping("/archivos/por-url")
    public ResponseEntity<Object> subirArchivoPorUrl(@RequestBody String body) {
        return solicitudesHttp.post(urlBaseEstaticas + "/archivos/por-url", body, Object.class);
    }

    @PostMapping("/archivos")
    public ResponseEntity<Object> subirArchivos(@RequestParam("files") MultipartFile[] files) {
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        for (MultipartFile file : files) {
            body.add("files", file.getResource());
        }

        return solicitudesHttp.postMultipart(urlBaseEstaticas + "/archivos", body, Object.class);
    }
}
