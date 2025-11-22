package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiAdministrativa")
public class ColeccionController {
    private ConfigService configService;
    private final String urlBaseAgregador;
    private final SolicitudesHttp solicitudesHttp;

    public ColeccionController(ConfigService configService) {
        this.configService = configService;
        this.urlBaseAgregador = configService.getUrl();
        this.solicitudesHttp = new SolicitudesHttp(new RestTemplateBuilder());
    }

    // --- CREATE ---
    @PostMapping("/colecciones")
    public ResponseEntity<Object> crearColeccion(@RequestBody String body) {
        return solicitudesHttp.post(urlBaseAgregador + "/colecciones", body, Object.class);
    }

    // --- UPDATE ---
    @PatchMapping("/colecciones/{id}/algoritmo")
    public ResponseEntity<Object> modificarAlgoritmo(@PathVariable("id") String id,
                                                   @RequestBody String body) {
        return solicitudesHttp.patch(urlBaseAgregador + "/colecciones/" + id + "/algoritmo", body, Object.class);
    }

    @PostMapping("/colecciones/{id}/fuentes")
    public ResponseEntity<Object> agregarFuente(@PathVariable("id") String id,
                                              @RequestBody String body) {
        return solicitudesHttp.post(urlBaseAgregador + "/colecciones/" + id + "/fuentes", body, Object.class);
    }

    @DeleteMapping("/colecciones/{id}/fuentes/{fuenteId}")
    public ResponseEntity<Void> quitarFuente(@PathVariable("id") String id,
                                             @PathVariable("fuenteId") String fuenteId) {
        return solicitudesHttp.delete(urlBaseAgregador + "/colecciones/" + id + "/fuentes/" + fuenteId, Void.class);
    }

    // --- DELETE ---
    @DeleteMapping("/colecciones/{id}")
    public ResponseEntity<Void> eliminarColeccion(@PathVariable("id") String id) {
        return solicitudesHttp.delete(urlBaseAgregador + "/colecciones/" + id, void.class);
    }
}