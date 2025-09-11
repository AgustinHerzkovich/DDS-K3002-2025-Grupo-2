package aplicacion.controllers;

import aplicacion.dto.input.IdentidadContribuyenteInputDto;
import aplicacion.dto.input.ContribuyenteInputDto;
import aplicacion.dto.output.ContribuyenteOutputDto;
import aplicacion.excepciones.ContribuyenteNoConfiguradoException;
import aplicacion.services.ContribuyenteService;
import domain.excepciones.IdInvalidoException;
import domain.peticiones.Validaciones;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ContribuyenteOutputDto> crearContribuyente(@RequestBody ContribuyenteInputDto contribuyenteInputDto) {
        ContribuyenteOutputDto contribuyenteProcesado = contribuyenteService.guardarContribuyente(contribuyenteInputDto);
        System.out.println("Se ha creado el contribuyente: " + contribuyenteProcesado.getId()); // esto cuando se haga el front lo podemos sacar
        return ResponseEntity.ok(contribuyenteProcesado);
    }

    @PatchMapping("/contribuyentes/{id}/identidad")
    public ResponseEntity<?> modificarIdentidadAContribuyente(@RequestBody IdentidadContribuyenteInputDto identidadContribuyenteInputDto,
                                                                                 @PathVariable("id") Long id) {
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
