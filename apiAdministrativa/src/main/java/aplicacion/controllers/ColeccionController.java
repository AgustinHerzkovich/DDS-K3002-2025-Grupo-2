package aplicacion.controllers;

import aplicacion.config.ConfigService;
import domain.peticiones.SolicitudesHttp;
import org.springframework.boot.web.client.RestTemplateBuilder;
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

    // --- READ ---
    @GetMapping("/colecciones")
    public ResponseEntity<Object> mostrarColecciones() {
        return solicitudesHttp.get(urlBaseAgregador + "/colecciones", Object.class);
    }

    @GetMapping("/colecciones/{id}")
    public ResponseEntity<Object> mostrarColeccion(@PathVariable String id) {
        return solicitudesHttp.get(urlBaseAgregador + "/colecciones/" + id, Object.class);
    }

    // --- UPDATE ---
    @PatchMapping("/colecciones/{id}/algoritmo")
    public ResponseEntity<Void> modificarAlgoritmo(@PathVariable String id,
                                                   @RequestBody String body) {
        solicitudesHttp.patch(urlBaseAgregador + "/colecciones/" + id + "/algoritmo", body);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/colecciones/{id}/fuentes/agregar")
    public ResponseEntity<Void> agregarFuente(@PathVariable String id,
                                              @RequestBody String body) {
        solicitudesHttp.patch(urlBaseAgregador + "/colecciones/" + id + "/fuentes", body);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/colecciones/{id}/fuentes/quitar")
    public ResponseEntity<Void> quitarFuente(@PathVariable String id,
                                             @RequestBody String body) {
        solicitudesHttp.patch(urlBaseAgregador + "/colecciones/" + id + "/fuentes", body);
        return ResponseEntity.ok().build();
    }

    // --- DELETE ---
    @DeleteMapping("/colecciones/{id}")
    public ResponseEntity<Void> eliminarColeccion(@PathVariable String id) {
        solicitudesHttp.delete(urlBaseAgregador + "/colecciones/" + id);
        return ResponseEntity.ok().build();
    }
}