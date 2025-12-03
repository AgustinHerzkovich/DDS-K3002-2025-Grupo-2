package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.helpers.UrlHelper;
import domain.peticiones.ResponseWrapper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/apiAdministrativa")
public class HechoController {
    private final String urlBaseAgregador;
    private final String urlBaseDinamicas;
    private final SolicitudesHttp solicitudesHttp;

    public HechoController(ConfigService configService) {
        this.urlBaseAgregador = configService.getUrlAgregador();
        this.urlBaseDinamicas = configService.getUrlFuentesDinamicas();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    @PostMapping("/hechos/{id}/tags")
    public ResponseEntity<?> agregarEtiqueta(@PathVariable(name = "id") String id, @RequestBody String body) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.post(urlBaseAgregador + "/hechos/" + id + "/tags", body, String.class));
    }

    @DeleteMapping("/hechos/{id}/tags/{nombreTag}")
    public ResponseEntity<?> quitarEtiqueta(@PathVariable(name = "id") String id,
                                             @PathVariable(name = "nombreTag") String nombreTag) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.delete(urlBaseAgregador + "/hechos/" + id + "/tags/" + nombreTag, String.class));
    }

    @PostMapping("/hechos")
    public ResponseEntity<?> reportarHecho(@RequestBody String body) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.post(urlBaseAgregador + "/hechos", body, String.class));
    }

    @GetMapping("/hechos")
    public ResponseEntity<?> obtenerHechosPendientes(@RequestParam(value = "fechaMayorA", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaMayorA,
                                                          @RequestParam(value = "pendiente", required = false, defaultValue = "false") Boolean pendiente) {
        StringBuilder url = new StringBuilder(urlBaseDinamicas + "/hechos");
        // No encodificar la fecha manualmente porque RestTemplate ya lo hace automáticamente
        UrlHelper.appendQueryParamSinEncode(url, "fechaMayorA", fechaMayorA);
        UrlHelper.appendQueryParamSinEncode(url, "pendiente", pendiente);
        return ResponseWrapper.wrapResponse(solicitudesHttp.get(url.toString(), String.class));
    }

    @PatchMapping("hechos/{id}/estadoRevision")
    public ResponseEntity<?> actualizarEstadoRevision(@PathVariable(name = "id") String id, @RequestBody String body) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.patch(urlBaseDinamicas + "/hechos/" + id + "/estadoRevision", body, String.class));
    }
}


