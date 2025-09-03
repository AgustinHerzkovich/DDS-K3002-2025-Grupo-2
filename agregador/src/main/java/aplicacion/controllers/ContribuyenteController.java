package aplicacion.controllers;

import aplicacion.dto.input.ContribuyenteInputDTO;
import aplicacion.dto.output.ContribuyenteOutputDTO;
import aplicacion.services.ContribuyenteService;
import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.dto.input.IdentidadContribuyenteInputDTO;
import aplicacion.dto.output.IdentidadContribuyenteOutputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fuentesDinamicas")
public class ContribuyenteController {
    private final ContribuyenteService contribuyenteService;

    public ContribuyenteController(ContribuyenteService contribuyenteService) {
        this.contribuyenteService = contribuyenteService;
    }

    @PostMapping("/contribuyentes")
    public ResponseEntity<ContribuyenteOutputDTO> crearContribuyente(@RequestBody ContribuyenteInputDTO contribuyente) {
        ContribuyenteOutputDTO contribuyenteGuardado =  contribuyenteService.guardarContribuyente(contribuyente);
        //Contribuyente contribuyenteGuardado = contribuyenteService.obtenerContribuyentePorId(contribuyente.getId());
        System.out.println("Se ha creado el contribuyente: " + contribuyenteGuardado.getId());
        return ResponseEntity.ok(contribuyenteGuardado);
    }
    /*
    @PatchMapping("/contribuyentes/{id}")
    public ResponseEntity<IdentidadContribuyenteOutputDTO> agregarIdentidadAContribuyente(@RequestBody IdentidadContribuyenteInputDTO identidad, @PathVariable("id") Long id) { // No habria que pasarle un contribuyente entero en lugar de solo la identidad? Si queres cambiar esAdministrador como lo haces?
        ContribuyenteOutputDTO contribuyente = contribuyenteService.agregarIdentidadAContribuyente(id, identidad);
        System.out.println("Se ha agregado la identidad: " + contribuyente.getNombreCompleto() + " al contribuyente: " + contribuyente.getId());
        return ResponseEntity.ok(contribuyente);
    }
     */

}
