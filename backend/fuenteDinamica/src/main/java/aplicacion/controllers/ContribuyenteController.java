package aplicacion.controllers;

import aplicacion.dto.output.HechoOutputDto;
import aplicacion.dto.input.IdentidadContribuyenteInputDto;
import aplicacion.dto.input.ContribuyenteInputDto;
import aplicacion.dto.output.ContribuyenteOutputDto;
import aplicacion.excepciones.MailYaExisteException;
import aplicacion.services.HechoService;
import domain.excepciones.IdInvalidoException;
import domain.peticiones.Validaciones;
import aplicacion.excepciones.ContribuyenteNoConfiguradoException;
import aplicacion.services.ContribuyenteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuentesDinamicas")
public class ContribuyenteController {
    private final ContribuyenteService contribuyenteService;
    private final HechoService hechoService;

    public ContribuyenteController(ContribuyenteService contribuyenteService, HechoService hechoService) {
        this.contribuyenteService = contribuyenteService;
        this.hechoService = hechoService;
    }

    @GetMapping("/contribuyentes/{id}/hechos")
    public ResponseEntity<?> obtenerHechosContribuyente(@PathVariable(name = "id") String id,
                                                        @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                        @RequestParam(name = "size", defaultValue = "20") Integer size) {
        try {
            Validaciones.validarId(id);
            Pageable pageable = PageRequest.of(page, size);
            Page<HechoOutputDto> hechos = hechoService.obtenerHechosDeContribuyente(id, pageable);
            return ResponseEntity.ok(hechos);
        }
        catch (IdInvalidoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (ContribuyenteNoConfiguradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/contribuyentes")
    public ResponseEntity<ContribuyenteOutputDto> crearContribuyente(@Valid @RequestBody ContribuyenteInputDto contribuyenteInputDto) {
        try {
            ContribuyenteOutputDto contribuyenteProcesado = contribuyenteService.guardarContribuyente(contribuyenteInputDto);
            System.out.println("Se ha creado el contribuyente: " + contribuyenteProcesado.getId()); // esto cuando se haga el front lo podemos sacar
            return ResponseEntity.status(201).body(contribuyenteProcesado);
        } catch (MailYaExisteException e) {
            ContribuyenteOutputDto contribuyente = contribuyenteService.obtenerContribuyentePorMail(contribuyenteInputDto.getMail());
            return ResponseEntity.ok(contribuyente);
        }
    }

    @PatchMapping("/contribuyentes/{id}/identidad")
    public ResponseEntity<?> modificarIdentidadAContribuyente(@Valid @RequestBody IdentidadContribuyenteInputDto identidadContribuyenteInputDto,
                                                              @PathVariable(name = "id") String id) {
        try {
            Validaciones.validarId(id);
            ContribuyenteOutputDto contribuyenteProcesado = contribuyenteService.modificarIdentidadAContribuyente(id, identidadContribuyenteInputDto);
            System.out.println("Se ha modificado la identidad: " + identidadContribuyenteInputDto.getNombre() + " " + identidadContribuyenteInputDto.getApellido() + " al contribuyente: " + id);
            return ResponseEntity.ok(contribuyenteProcesado);
        } catch (IdInvalidoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (ContribuyenteNoConfiguradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}