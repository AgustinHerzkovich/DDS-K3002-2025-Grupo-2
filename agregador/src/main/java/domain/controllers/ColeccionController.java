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
    private final HechoService hecho_service;
    private final FuenteService fuente_service;

    public ColeccionController(HechoService hecho_service, FuenteService fuente_service) {
        this.hecho_service = hecho_service;
        this.fuente_service = fuente_service;
    }

    // Operaciones CREATE sobre Colecciones
    @PostMapping("/colecciones")
    public ResponseEntity<Coleccion> crearColeccion(Coleccion coleccion) {
        // logica de crear una coleccion en el repositorio //todo
        hecho_service.guardarColeccion(coleccion);
        fuente_service.guardarFuentes(coleccion.getFuentes());
        return ResponseEntity.ok(coleccion);
    }

    // Operaciones READ sobre Colecciones
    @GetMapping("/colecciones")
    public List<Coleccion> mostrarColecciones() {
        // logica de buscar las colecciones del repositorio //todo
        return hecho_service.obtenerColecciones();
    }

    @GetMapping("/colecciones/{id}/hechosIrrestrictos")
    public List<Hecho> mostrarHechosIrrestrictos(@PathVariable("id") String id_coleccion) {
        // logica de buscar los hechos del repositorio //todo
        return hecho_service.obtenerHechosIrrestrictosPorColeccion(id_coleccion);
    }

    @GetMapping("/colecciones/{id}/hechosCurados")
    public List<Hecho> mostrarHechosCurados(@PathVariable("id") String id_coleccion){


        return hecho_service.obtenerHechosCuradosPorColeccion(id_coleccion);
    }

    // Operaciones UPDATE sobre Colecciones

    // Operaciones DELETE sobre Colecciones
}
