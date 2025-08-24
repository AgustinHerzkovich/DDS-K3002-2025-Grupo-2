package domain.controllers;

import domain.services.ContribuyenteService;
import domain.usuarios.Contribuyente;
import domain.usuarios.IdentidadContribuyente;
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
    public ResponseEntity<Void> crearContribuyente(@RequestBody Contribuyente contribuyente) {
        Contribuyente contribuyenteGuardado = contribuyenteService.guardarContribuyente(contribuyente);
        System.out.println("Se ha creado el contribuyente: " + contribuyenteGuardado.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/contribuyentes/{id}")
    public ResponseEntity<Void> agregarIdentidadAContribuyente(@RequestBody IdentidadContribuyente identidad, @PathVariable("id") Long id) {
        Contribuyente contribuyente = contribuyenteService.agregarIdentidadAContribuyente(id, identidad);
        System.out.println("Se ha agregado la identidad: " + contribuyente.getNombreCompleto() + " al contribuyente: " + contribuyente.getId());
        return ResponseEntity.ok().build();
    }
}
