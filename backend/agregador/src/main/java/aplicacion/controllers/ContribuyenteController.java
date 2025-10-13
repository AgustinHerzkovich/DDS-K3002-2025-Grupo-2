package aplicacion.controllers;

import aplicacion.dto.input.ContribuyenteInputDto;
import aplicacion.dto.output.ContribuyenteOutputDto;
import aplicacion.services.ContribuyenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agregador")
public class ContribuyenteController {
    private final ContribuyenteService contribuyenteService;

    public ContribuyenteController(ContribuyenteService contribuyenteService) {
        this.contribuyenteService = contribuyenteService;
    }

    @PostMapping("/contribuyentes")
    public ResponseEntity<ContribuyenteOutputDto> crearContribuyente(@RequestBody ContribuyenteInputDto contribuyenteInputDto) {
        ContribuyenteOutputDto contribuyenteProcesado = contribuyenteService.guardarContribuyente(contribuyenteInputDto);
        System.out.println("Se ha creado el contribuyente: " + contribuyenteProcesado.getId()); // esto cuando se haga el front lo podemos sacar
        return ResponseEntity.ok(contribuyenteProcesado);
    }
}
