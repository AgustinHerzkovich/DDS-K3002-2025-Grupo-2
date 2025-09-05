package aplicacion.controllers;

import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.input.HechoInputDTO;
import aplicacion.dto.output.HechoOutputDTO;
import aplicacion.services.HechoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agregador")
public class HechoController {
    private final HechoService hechoService;

    public HechoController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping("/hechos")
    public List<HechoOutputDTO> obtenerHechos() {
        return hechoService.obtenerHechosAsDTO();
    }

    @PostMapping("/hechos")
    public ResponseEntity<HechoOutputDTO> reportarHecho(@RequestBody HechoInputDTO hechoInputDto) {
        HechoOutputDTO hecho = hechoService.agregarHecho(hechoInputDto);
        System.out.println("Hecho creado: " + hecho.getId());
        return ResponseEntity.ok(hecho);
    }
}
