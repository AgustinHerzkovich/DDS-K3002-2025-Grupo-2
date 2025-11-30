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
    public ResponseEntity<Object> crearColeccion(@RequestBody String body) {
        return solicitudesHttp.post(urlBaseAgregador + "/colecciones", body, Object.class);
    }

    // --- UPDATE ---
    @PatchMapping("/colecciones/{id}/algoritmo")
    public ResponseEntity<Object> modificarAlgoritmo(@PathVariable(name = "id") String id,
                                                   @RequestBody String body) {
        return solicitudesHttp.patch(urlBaseAgregador + "/colecciones/" + id + "/algoritmo", body, Object.class);
    }

    @PostMapping("/colecciones/{id}/fuentes")
    public ResponseEntity<Object> agregarFuente(@PathVariable(name = "id") String id,
                                              @RequestBody String body) {
        return solicitudesHttp.post(urlBaseAgregador + "/colecciones/" + id + "/fuentes", body, Object.class);
    }

    @DeleteMapping("/colecciones/{id}/fuentes/{fuenteId}")
    public ResponseEntity<Void> quitarFuente(@PathVariable(name = "id") String id,
                                             @PathVariable(name = "fuenteId") String fuenteId) {
        return solicitudesHttp.delete(urlBaseAgregador + "/colecciones/" + id + "/fuentes/" + fuenteId, Void.class);
    }

    // --- DELETE ---
    @DeleteMapping("/colecciones/{id}")
    public ResponseEntity<Void> eliminarColeccion(@PathVariable(name = "id") String id) {
        return solicitudesHttp.delete(urlBaseAgregador + "/colecciones/" + id, void.class);
    }
}