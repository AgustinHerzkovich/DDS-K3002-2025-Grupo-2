package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.helpers.UrlHelper;
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
    public ResponseEntity<Object> agregarEtiqueta(@PathVariable(name = "id") String id, @RequestBody String body) {
        return solicitudesHttp.post(urlBaseAgregador + "/hechos/" + id + "/tags", body, Object.class);
    }

    @DeleteMapping("/hechos/{id}/tags/{nombreTag}")
    public ResponseEntity<Object> quitarEtiqueta(@PathVariable(name = "id") String id,
                                             @PathVariable(name = "nombreTag") String nombreTag) {
        return solicitudesHttp.delete(urlBaseAgregador + "/hechos/" + id + "/tags/" + nombreTag, Object.class);
    }

    @PostMapping("/hechos")
    public ResponseEntity<Object> reportarHecho(@RequestBody String body) {
        return solicitudesHttp.post(urlBaseAgregador + "/hechos", body, Object.class);
    }

    @GetMapping("/hechos")
    public ResponseEntity<Object> obtenerHechosPendientes(@RequestParam(value = "fechaMayorA", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime fechaMayorA,
                                                          @RequestParam(value = "pendiente", required = false, defaultValue = "false") Boolean pendiente) {
        StringBuilder url = new StringBuilder(urlBaseDinamicas + "/hechos");
        UrlHelper.appendQueryParam(url, "fechaMayorA", fechaMayorA);
        UrlHelper.appendQueryParam(url, "pendiente", pendiente);
        return solicitudesHttp.get(url.toString(), Object.class);
    }

    @PatchMapping("hechos/{id}/estadoRevision")
    public ResponseEntity<String> actualizarEstadoRevision(@PathVariable(name = "id") String id, @RequestBody String body) {
        return solicitudesHttp.patch(urlBaseDinamicas + "/hechos/" + id + "/estadoRevision", body, String.class);
    }
}


