package aplicacion.controllers;

import aplicacion.dto.input.ContribuyenteInputDto;
import aplicacion.dto.input.IdentidadContribuyenteInputDto;
import aplicacion.dto.output.ContribuyenteOutputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.excepciones.AutorizacionDenegadaException;
import aplicacion.excepciones.MailYaExisteException;
import aplicacion.services.ContribuyenteService;
import aplicacion.services.FuenteMutexManager;
import domain.excepciones.IdInvalidoException;
import domain.helpers.JwtUtil;
import domain.peticiones.Validaciones;
import aplicacion.services.HechoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import aplicacion.excepciones.ContribuyenteNoConfiguradoException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping
public class ContribuyenteController {
    private final ContribuyenteService contribuyenteService;
    private final HechoService hechoService;
    private final Logger logger = LoggerFactory.getLogger(ContribuyenteController.class);

    public ContribuyenteController(ContribuyenteService contribuyenteService, HechoService hechoService) {
        this.contribuyenteService = contribuyenteService;
        this.hechoService = hechoService;
    }

    @GetMapping("/contribuyentes/{id}/hechos")
    public ResponseEntity<?> obtenerHechosContribuyente(@PathVariable(name = "id") String id,
                                                        @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                        @RequestParam(name = "size", defaultValue = "20") Integer size,
                                                        @RequestHeader(name = "Authorization", required = false) String token) {
        try {
            // Validar que el usuario sea el autor del hecho
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se proporcionó token de autenticación");
            }

            // Extraer el ID del usuario del token JWT
            String userId = JwtUtil.extractUserId(token);
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
            }

            Validaciones.validarId(id);
            Pageable pageable = PageRequest.of(page, size);
            Page<HechoOutputDto> hechos = hechoService.obtenerHechosDeContribuyente(id, pageable, userId);
            return ResponseEntity.ok(hechos);
        } catch (IdInvalidoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ContribuyenteNoConfiguradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AutorizacionDenegadaException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/contribuyentes")
    public ResponseEntity<ContribuyenteOutputDto> crearContribuyente(@Valid @RequestBody ContribuyenteInputDto contribuyenteInputDto) {
        try {
            ContribuyenteOutputDto contribuyenteProcesado = contribuyenteService.guardarContribuyente(contribuyenteInputDto);
            logger.info("Se ha creado el contribuyente: {}", contribuyenteProcesado.getId()); // esto cuando se haga el front lo podemos sacar
            return ResponseEntity.status(201).body(contribuyenteProcesado);
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
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
    }

    @PatchMapping("/contribuyentes/{id}/identidad")
    public ResponseEntity<?> modificarIdentidadAContribuyente(@Valid @RequestBody IdentidadContribuyenteInputDto identidadContribuyenteInputDto, @PathVariable(name = "id") String id) {
        try {
            Validaciones.validarId(id);
            ContribuyenteOutputDto contribuyenteProcesado = contribuyenteService.modificarIdentidadAContribuyente(id, identidadContribuyenteInputDto);
            logger.info("Se ha modificado la identidad: {} {} al contribuyente: {}", identidadContribuyenteInputDto.getNombre(), identidadContribuyenteInputDto.getApellido(), id);
            return ResponseEntity.ok(contribuyenteProcesado);
        } catch (IdInvalidoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (ContribuyenteNoConfiguradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
