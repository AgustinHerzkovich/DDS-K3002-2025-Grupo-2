package domain.controllers;

import domain.services.ContribuyenteService;
import domain.usuarios.Contribuyente;
import domain.usuarios.IdentidadContribuyente;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fuentesDinamicas")
public class ContribuyenteController {
    private final ContribuyenteService contribuyenteService;

    public ContribuyenteController(ContribuyenteService contribuyenteService) {
        this.contribuyenteService = contribuyenteService;
    }

    @PostMapping("/contribuyentes")
    public ResponseEntity<Map<String, Integer>> crearContribuyente(@RequestBody Contribuyente contribuyente) {
        Contribuyente contribuyenteGuardado = contribuyenteService.guardarContribuyente(contribuyente);
        Map<String, Integer> resp = new HashMap<>();
        resp.put("contribuyenteId", Math.toIntExact(contribuyenteGuardado.getId()));
        System.out.println("Se ha creado el contribuyente: " + contribuyenteGuardado.getId()); // esto cuando se haga el front lo podemos sacar
        return ResponseEntity.ok(resp);
    }

    /*public ResponseEntity<Integer> crearContribuyente(@RequestBody Contribuyente contribuyente) {
        Contribuyente contribuyenteGuardado = contribuyenteService.guardarContribuyente(contribuyente);
        System.out.println("Se ha creado el contribuyente: " + contribuyenteGuardado.getId());
        return ResponseEntity.ok(Math.toIntExact(contribuyenteGuardado.getId()));
    } de lucas*/

    @PatchMapping("/contribuyentes/{id}")
    public ResponseEntity<Void> agregarIdentidadAContribuyente(@RequestBody IdentidadContribuyente identidad, @PathVariable("id") Long id) {
        Contribuyente contribuyente = contribuyenteService.agregarIdentidadAContribuyente(id, identidad);
        System.out.println("Se ha agregado la identidad: " + contribuyente.getNombreCompleto() + " al contribuyente: " + contribuyente.getId());
        return ResponseEntity.ok().build();
    }
}
