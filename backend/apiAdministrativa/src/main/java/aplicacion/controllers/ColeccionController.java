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
public class ColeccionController {
    private final ConfigService configService;
    private final SolicitudesHttp solicitudesHttp;

    public ColeccionController(@Lazy ConfigService configService) {
        this.configService = configService;
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    // --- CREATE ---
    @PostMapping("/colecciones")
    public ResponseEntity<?> crearColeccion(@RequestBody String body) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.post(configService.getUrlAgregador() + "/colecciones", body, String.class));
    }

    // --- UPDATE ---
    @PatchMapping("/colecciones/{id}/algoritmo")
    public ResponseEntity<?> modificarAlgoritmo(@PathVariable(name = "id") String id,
                                                   @RequestBody String body) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.patch(configService.getUrlAgregador() + "/colecciones/" + id + "/algoritmo", body, String.class));
    }

    @PostMapping("/colecciones/{id}/fuentes")
    public ResponseEntity<?> agregarFuente(@PathVariable(name = "id") String id,
                                              @RequestBody String body) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.post(configService.getUrlAgregador() + "/colecciones/" + id + "/fuentes", body, String.class));
    }

    @DeleteMapping("/colecciones/{id}/fuentes/{fuenteId}")
    public ResponseEntity<?> quitarFuente(@PathVariable(name = "id") String id,
                                             @PathVariable(name = "fuenteId") String fuenteId) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.delete(configService.getUrlAgregador() + "/colecciones/" + id + "/fuentes/" + fuenteId, String.class));
    }

    // --- DELETE ---
    @DeleteMapping("/colecciones/{id}")
    public ResponseEntity<?> eliminarColeccion(@PathVariable(name = "id") String id) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.delete(configService.getUrlAgregador() + "/colecciones/" + id, String.class));
    }
}