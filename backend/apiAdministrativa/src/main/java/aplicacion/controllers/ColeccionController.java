package aplicacion.controllers;

import aplicacion.config.ConfigService;
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
    public ResponseEntity<String> crearColeccion(@RequestBody String body) {
        return solicitudesHttp.post(urlBaseAgregador + "/colecciones", body, String.class);
    }

    // --- UPDATE ---
    @PatchMapping("/colecciones/{id}/algoritmo")
    public ResponseEntity<String> modificarAlgoritmo(@PathVariable(name = "id") String id,
                                                   @RequestBody String body) {
        return solicitudesHttp.patch(urlBaseAgregador + "/colecciones/" + id + "/algoritmo", body, String.class);
    }

    @PostMapping("/colecciones/{id}/fuentes")
    public ResponseEntity<String> agregarFuente(@PathVariable(name = "id") String id,
                                              @RequestBody String body) {
        return solicitudesHttp.post(urlBaseAgregador + "/colecciones/" + id + "/fuentes", body, String.class);
    }

    @DeleteMapping("/colecciones/{id}/fuentes/{fuenteId}")
    public ResponseEntity<String> quitarFuente(@PathVariable(name = "id") String id,
                                             @PathVariable(name = "fuenteId") String fuenteId) {
        return solicitudesHttp.delete(urlBaseAgregador + "/colecciones/" + id + "/fuentes/" + fuenteId, String.class);
    }

    // --- DELETE ---
    @DeleteMapping("/colecciones/{id}")
    public ResponseEntity<String> eliminarColeccion(@PathVariable(name = "id") String id) {
        return solicitudesHttp.delete(urlBaseAgregador + "/colecciones/" + id, String.class);
    }
}