package domain.controllers;

import domain.colecciones.Coleccion;
import domain.hechos.Hecho;
import domain.services.FuenteService;
import domain.services.HechoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agregador")
public class ColeccionController {
    private final HechoService hechoService;
    private final FuenteService fuenteService;

    public ColeccionController(HechoService hechoService, FuenteService fuenteService) {
        this.hechoService = hechoService;
        this.fuenteService = fuenteService;
    }

    // Operaciones CREATE sobre Colecciones
    @PostMapping("/colecciones")
    public ResponseEntity<Coleccion> crearColeccion(Coleccion coleccion) {
        // logica de crear una coleccion en el repositorio //todo
        hechoService.guardarColeccion(coleccion);
        fuenteService.guardarFuentes(coleccion.getFuentes());
        return ResponseEntity.ok(coleccion);
    }

    // Operaciones READ sobre Colecciones
    @GetMapping("/colecciones")
    public List<Coleccion> mostrarColecciones() {
        // logica de buscar las colecciones del repositorio //todo
        return hechoService.obtenerColecciones();
    }

    @GetMapping("/colecciones/{id}/hechosIrrestrictos")
    public List<Hecho> mostrarHechosIrrestrictos(@PathVariable("id") String idColeccion) {
        // logica de buscar los hechos del repositorio //todo
        return hechoService.obtenerHechosIrrestrictosPorColeccion(idColeccion);
    }

    @GetMapping("/colecciones/{id}/hechosCurados")
    public List<Hecho> mostrarHechosCurados(@PathVariable("id") String idColeccion){


        return hechoService.obtenerHechosCuradosPorColeccion(idColeccion);
    }

    // Operaciones UPDATE sobre Colecciones

    // Operaciones DELETE sobre Colecciones
}
