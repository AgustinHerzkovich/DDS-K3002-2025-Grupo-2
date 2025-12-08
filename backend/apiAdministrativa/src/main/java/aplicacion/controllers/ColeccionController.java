package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.peticiones.ResponseWrapper;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiAdministrativa")
public class ColeccionController {
    private final String urlBaseAgregador;
    private final SolicitudesHttp solicitudesHttp;

    public ColeccionController(ConfigService configService) {
        this.urlBaseAgregador = configService.getUrlAgregador();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    // --- CREATE ---
    @PostMapping("/colecciones")
    public ResponseEntity<?> crearColeccion(@RequestBody String body) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.post(urlBaseAgregador + "/colecciones", body, String.class));
    }

    // --- UPDATE ---
    @PatchMapping("/colecciones/{id}/algoritmo")
    public ResponseEntity<?> modificarAlgoritmo(@PathVariable(name = "id") String id,
                                                   @RequestBody String body) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.patch(urlBaseAgregador + "/colecciones/" + id + "/algoritmo", body, String.class));
    }

    @PostMapping("/colecciones/{id}/fuentes")
    public ResponseEntity<?> agregarFuente(@PathVariable(name = "id") String id,
                                              @RequestBody String body) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.post(urlBaseAgregador + "/colecciones/" + id + "/fuentes", body, String.class));
    }

    @DeleteMapping("/colecciones/{id}/fuentes/{fuenteId}")
    public ResponseEntity<?> quitarFuente(@PathVariable(name = "id") String id,
                                             @PathVariable(name = "fuenteId") String fuenteId) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.delete(urlBaseAgregador + "/colecciones/" + id + "/fuentes/" + fuenteId, String.class));
    }

    // --- DELETE ---
    @DeleteMapping("/colecciones/{id}")
    public ResponseEntity<?> eliminarColeccion(@PathVariable(name = "id") String id) {
        return ResponseWrapper.wrapResponse(solicitudesHttp.delete(urlBaseAgregador + "/colecciones/" + id, String.class));
    }
}