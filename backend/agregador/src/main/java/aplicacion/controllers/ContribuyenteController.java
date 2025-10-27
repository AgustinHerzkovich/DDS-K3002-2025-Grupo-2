package aplicacion.controllers;

import aplicacion.dto.input.ContribuyenteInputDto;
import aplicacion.dto.output.ContribuyenteOutputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.excepciones.MailYaExisteException;
import aplicacion.services.ContribuyenteService;
import domain.excepciones.IdInvalidoException;
import domain.peticiones.Validaciones;
import aplicacion.services.HechoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import aplicacion.excepciones.ContribuyenteNoConfiguradoException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/agregador")
public class ContribuyenteController {
    private final ContribuyenteService contribuyenteService;
    private final HechoService hechoService;

    public ContribuyenteController(ContribuyenteService contribuyenteService, HechoService hechoService) {
        this.contribuyenteService = contribuyenteService;
        this.hechoService = hechoService;


    }

    @GetMapping("/contribuyentes/{id}/hechos")
    public ResponseEntity<List<HechoOutputDto>> obtenerHechosContribuyente(@PathVariable("id") Long id) {
        try {
            Validaciones.validarId(id);
            List<HechoOutputDto> hechos = hechoService.obtenerHechosDeContribuyente(id);
            return ResponseEntity.ok(hechos);
        } catch (IdInvalidoException e) {
            return ResponseEntity.badRequest().build();
        } catch (ContribuyenteNoConfiguradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/contribuyentes")
    public ResponseEntity<ContribuyenteOutputDto> crearContribuyente(@RequestBody ContribuyenteInputDto contribuyenteInputDto) {
        try {
            ContribuyenteOutputDto contribuyenteProcesado = contribuyenteService.guardarContribuyente(contribuyenteInputDto);
            System.out.println("Se ha creado el contribuyente: " + contribuyenteProcesado.getId()); // esto cuando se haga el front lo podemos sacar
            return ResponseEntity.ok(contribuyenteProcesado);
        } catch (MailYaExisteException e) {
            ContribuyenteOutputDto contribuyente = contribuyenteService.obtenerContribuyentePorMail(contribuyenteInputDto.getMail());
            return ResponseEntity.ok(contribuyente);
        }
    }

    @GetMapping("/contribuyentes")
    public  ResponseEntity<?> obtenerContribuyentes(@RequestParam(name = "mail", required = false) String mail) {
        if (mail != null) {
            mail = URLDecoder.decode(mail, StandardCharsets.UTF_8); // <- decodifica %40 -> @
        }

        if (mail == null) {
            List<ContribuyenteOutputDto> contribuyentes = contribuyenteService.obtenerContribuyentes();
            return ResponseEntity.ok(contribuyentes);
        } else {
            try {
                ContribuyenteOutputDto contribuyente = contribuyenteService.obtenerContribuyentePorMail(mail);
                return ResponseEntity.ok(contribuyente);
            } catch (ContribuyenteNoConfiguradoException e) {
                return ResponseEntity.notFound().build();
            }
        }
    }
}
