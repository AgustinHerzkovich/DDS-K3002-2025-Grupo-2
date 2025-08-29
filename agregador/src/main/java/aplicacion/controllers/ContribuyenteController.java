package aplicacion.controllers;

import aplicacion.services.ContribuyenteService;
import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.domain.usuarios.IdentidadContribuyente;
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
    public ResponseEntity<Integer> crearContribuyente(@RequestBody Contribuyente contribuyente) {
        contribuyenteService.guardarContribuyente(contribuyente);
        Contribuyente contribuyenteGuardado = contribuyenteService.obtenerContribuyentePorId(contribuyente.getId());
        System.out.println("Se ha creado el contribuyente: " + contribuyenteGuardado.getId());
        return ResponseEntity.ok(Math.toIntExact(contribuyenteGuardado.getId()));
    }

    @PatchMapping("/contribuyentes/{id}")
    public ResponseEntity<Void> agregarIdentidadAContribuyente(@RequestBody IdentidadContribuyente identidad, @PathVariable("id") Long id) { // No habria que pasarle un contribuyente entero en lugar de solo la identidad? Si queres cambiar esAdministrador como lo haces?
        Contribuyente contribuyente = contribuyenteService.agregarIdentidadAContribuyente(id, identidad);
        System.out.println("Se ha agregado la identidad: " + contribuyente.getNombreCompleto() + " al contribuyente: " + contribuyente.getId());
        return ResponseEntity.ok().build();
    }

}
