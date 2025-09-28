package aplicacion.controllers;

import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.services.HechoService;
import aplicacion.services.schedulers.CargarHechosScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agregador")
public class HechoController {
    private final HechoService hechoService;
    private final CargarHechosScheduler cargarHechosScheduler;

    public HechoController(HechoService hechoService, CargarHechosScheduler cargarHechosScheduler) {
        this.hechoService = hechoService;
        this.cargarHechosScheduler = cargarHechosScheduler;
    }

    @GetMapping("/hechos")
    public List<HechoOutputDto> obtenerHechos(@RequestParam(name = "search", required = false) String textoBuscado) {
        List<HechoOutputDto> hechos;
        if (textoBuscado == null) {
            hechos = hechoService.obtenerHechosAsDTO();
        }
        else
        {
            hechos = hechoService.obtenerHechosPorTextoLibreDto(textoBuscado);
        }
        return hechos;
    }

    @PostMapping("/hechos")
    public ResponseEntity<HechoOutputDto> reportarHecho(@RequestBody HechoInputDto hechoInputDto) {
        HechoOutputDto hecho = hechoService.agregarHecho(hechoInputDto);
        System.out.println("Hecho creado: " + hecho.getId());
        return ResponseEntity.ok(hecho);
    }

    @PostMapping("/cargarHechos")
    public ResponseEntity<Void> cargarHechos() { // Endpoint para disparar la carga de hechos manualmente (si es necesario)
        cargarHechosScheduler.cargarHechos();
        return ResponseEntity.ok().build();
    }
}
