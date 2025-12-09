package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.helpers.UrlHelper;
import domain.peticiones.ResponseWrapper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/apiAdministrativa")
public class HechoController {
    private final ConfigService configService;
    private final SolicitudesHttp solicitudesHttp;

    public HechoController(@Lazy ConfigService configService) {
        this.configService = configService;
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PostMapping("/hechos/{id}/tags")
    public ResponseEntity<?> agregarEtiqueta(@PathVariable(name = "id") String id, @RequestBody String body) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.post(configService.getUrlAgregador() + "/hechos/" + id + "/tags", body, String.class));
    }

    @DeleteMapping("/hechos/{id}/tags/{nombreTag}")
    public ResponseEntity<?> quitarEtiqueta(@PathVariable(name = "id") String id,
                                             @PathVariable(name = "nombreTag") String nombreTag) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.delete(configService.getUrlAgregador() + "/hechos/" + id + "/tags/" + nombreTag, String.class));
    }

    @PostMapping("/hechos")
    public ResponseEntity<?> reportarHecho(@RequestBody String body) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.post(configService.getUrlAgregador() + "/hechos", body, String.class));
    }
    @PostMapping("/cargarHechos")
    public ResponseEntity<?> cargarHechos() {
        return ResponseWrapper.wrapResponse(solicitudesHttp.post(configService.getUrlAgregador() + "/cargarHechos", null, String.class));
    }

    @PatchMapping("/hechos/{id}")
    public ResponseEntity<?> editarHecho(@PathVariable(name = "id") String id, @RequestBody String body) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.patch(configService.getUrlAgregador() + "/hechos/" + id, body, String.class));
    }

    @GetMapping("/hechos")
    public ResponseEntity<?> obtenerHechosPendientes(@RequestParam(value = "fechaMayorA", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaMayorA,
                                                     @RequestParam(value = "pendiente", required = false, defaultValue = "false") Boolean pendiente,
                                                     @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        StringBuilder url = new StringBuilder(configService.getUrlFuentesDinamicas() + "/hechosPaginados");
        // No encodificar la fecha manualmente porque RestTemplate ya lo hace automáticamente
        UrlHelper.appendQueryParamSinEncode(url, "fechaMayorA", fechaMayorA);
        UrlHelper.appendQueryParamSinEncode(url, "pendiente", pendiente);
        UrlHelper.appendQueryParam(url, "page", page);
        UrlHelper.appendQueryParam(url, "size", size);
        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), String.class));
    }

    @PatchMapping("hechos/{id}/estadoRevision")
    public ResponseEntity<?> actualizarEstadoRevision(@PathVariable(name = "id") String id, @RequestBody String body) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.patch(configService.getUrlFuentesDinamicas() + "/hechos/" + id + "/estadoRevision", body, String.class));
    }
}


