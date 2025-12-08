package aplicacion.controllers;

import aplicacion.domain.hechos.Etiqueta;
import aplicacion.dto.input.HechoEdicionInputDto;
import aplicacion.dto.input.HechoReporteInputDto;
import aplicacion.dto.mappers.EtiquetaOutputMapper;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.excepciones.*;
import aplicacion.services.HechoService;
import aplicacion.services.schedulers.CargarHechosScheduler;
import aplicacion.services.schedulers.EjecutarAlgoritmoConsensoScheduler;
import domain.helpers.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
public class HechoController {
    private final HechoService hechoService;
    private final CargarHechosScheduler cargarHechosScheduler;
    private final EjecutarAlgoritmoConsensoScheduler ejecutarAlgoritmoConsensoScheduler;

    public HechoController(HechoService hechoService, CargarHechosScheduler cargarHechosScheduler, EjecutarAlgoritmoConsensoScheduler ejecutarAlgoritmoConsensoScheduler) {
        this.hechoService = hechoService;
        this.cargarHechosScheduler = cargarHechosScheduler;
        this.ejecutarAlgoritmoConsensoScheduler = ejecutarAlgoritmoConsensoScheduler;
    }

    @GetMapping("/hechos")
    public ResponseEntity<Page<HechoOutputDto>> obtenerHechos(@RequestParam(name = "categoria", required = false) String categoria,
                                               @RequestParam(name = "fechaReporteDesde", required = false) String fechaReporteDesde,
                                               @RequestParam(name = "fechaReporteHasta", required = false) String fechaReporteHasta,
                                               @RequestParam(name = "fechaAcontecimientoDesde", required = false) String fechaAcontecimientoDesde,
                                               @RequestParam(name = "fechaAcontecimientoHasta", required = false) String fechaAcontecimientoHasta,
                                               @RequestParam(name = "latitud", required = false) Double latitud,
                                               @RequestParam(name = "longitud", required = false) Double longitud,
                                               @RequestParam(name = "radio", required = false) Double radio,
                                               @RequestParam(name = "search", required = false) String textoBuscado,
                                              @RequestParam(name = "page", defaultValue = "0") Integer page,
                                              @RequestParam(name = "size", defaultValue = "100") Integer size) {

        // Decodificar y convertir strings de fecha a LocalDateTime
        LocalDateTime fechaReporteDesdeDateTime = fechaReporteDesde != null ?
            LocalDateTime.parse(URLDecoder.decode(fechaReporteDesde, StandardCharsets.UTF_8)) : null;
        LocalDateTime fechaReporteHastaDateTime = fechaReporteHasta != null ?
            LocalDateTime.parse(URLDecoder.decode(fechaReporteHasta, StandardCharsets.UTF_8)) : null;
        LocalDateTime fechaAcontecimientoDesdeDateTime = fechaAcontecimientoDesde != null ?
            LocalDateTime.parse(URLDecoder.decode(fechaAcontecimientoDesde, StandardCharsets.UTF_8)) : null;
        LocalDateTime fechaAcontecimientoHastaDateTime = fechaAcontecimientoHasta != null ?
            LocalDateTime.parse(URLDecoder.decode(fechaAcontecimientoHasta, StandardCharsets.UTF_8)) : null;

        Pageable pageable = PageRequest.of(page, size);

        Page<HechoOutputDto> hechos = hechoService.obtenerHechosAsDto(categoria, fechaReporteDesdeDateTime, fechaReporteHastaDateTime, fechaAcontecimientoDesdeDateTime, fechaAcontecimientoHastaDateTime, latitud, longitud, radio, textoBuscado, pageable);

        return ResponseEntity.ok(hechos);
    }

    @GetMapping("/hechos/{id}")
    public ResponseEntity<?> obtenerHechoPorId(@PathVariable(name = "id") String id) {
        try {
            HechoOutputDto hecho = hechoService.obtenerHechoDto(id);
            return ResponseEntity.ok(hecho);
        } catch (HechoNoEncontradoException | HechoNoVisibleException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/hechos")
    @PreAuthorize("@securityConfig.seguridadActiva ? hasRole('ADMIN') : true")
    public ResponseEntity<HechoOutputDto> reportarHecho(@Valid @RequestBody HechoReporteInputDto hechoReporteInputDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authorities: " + auth.getAuthorities());
        HechoOutputDto hecho = hechoService.agregarHechoReportado(hechoReporteInputDto);
        System.out.println("Hecho creado: " + hecho.getId());
        return ResponseEntity.status(201).body(hecho);
    }

    @PatchMapping("/hechos/{id}")
    @PreAuthorize("@securityConfig.seguridadActiva ? hasRole('ADMIN') : true")
    public ResponseEntity<?> editarHecho(@PathVariable(name = "id") String id,
                                         @Valid @RequestBody HechoEdicionInputDto hechoEdicionInputDto,
                                         @RequestHeader(name = "Authorization", required = false) String token) {
        System.out.println("EDITANDO el hecho: " + id );
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

            // Si la validación pasa, proceder con la edición
            HechoOutputDto hecho = hechoService.editarHecho(id, hechoEdicionInputDto, userId);
            System.out.println("Se ha editado correctamente el hecho: " + hecho.getTitulo() + "(" + id + ")" + " por el usuario: " + userId);
            return ResponseEntity.ok(hecho);
        } catch (HechoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (PlazoEdicionVencidoException | AnonimatoException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (AutorizacionDenegadaException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/hechos/{id}/tags")
    @PreAuthorize("@securityConfig.seguridadActiva ? hasRole('ADMIN') : true")
    public ResponseEntity<?> agregarEtiqueta(@PathVariable(name = "id") String hechoId, @RequestBody String etiquetaName) {
        try {
            Etiqueta etiqueta = hechoService.agregarEtiqueta(hechoId, etiquetaName);
            System.out.println("Se agrego el tag: " + etiquetaName);
            return ResponseEntity.ok(EtiquetaOutputMapper.map(etiqueta));
        } catch (HechoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
    @DeleteMapping("/hechos/{hechoId}/tags/{tag}")
    @PreAuthorize("@securityConfig.seguridadActiva ? hasRole('ADMIN') : true")
    public ResponseEntity<?> eliminarEtiqueta(@PathVariable(name = "hechoId") String hechoId, @PathVariable(name = "tag") String etiquetaName) {
        try {
            hechoService.eliminarEtiqueta(hechoId, etiquetaName);
            System.out.println("Se elimino el tag: " + etiquetaName);
            return ResponseEntity.noContent().build();
        } catch (HechoNoEncontradoException | EtiquetaNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PostMapping("/cargarHechos")
    @PreAuthorize("@securityConfig.seguridadActiva ? hasRole('ADMIN') : true")
    public ResponseEntity<Void> cargarHechos() { // Endpoint para disparar la carga de hechos manualmente (si es necesario)
        cargarHechosScheduler.cargarHechos();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/curarHechos")
    public ResponseEntity<Void> curarHechos() { // Endpoint para disparar la ejecución del algoritmo de consenso manualmente (si es necesario)
        ejecutarAlgoritmoConsensoScheduler.curarHechos();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/hechos/index")
    public ResponseEntity<List<String>> autoCompletar(@RequestParam(name = "search") String currentSearch, @RequestParam(name = "limit", required = false, defaultValue = "5") Integer limit){
        if(limit >100 || limit <0)
            throw new TooHighLimitException(limit);
        List<String> recomendaciones = hechoService.obtenerAutocompletado(currentSearch, limit);
        return ResponseEntity.ok(recomendaciones);
    }

    @GetMapping("/hechosSinPaginar")
    public ResponseEntity<List<HechoOutputDto>> obtenerHechosSinPaginar() {
        return ResponseEntity.ok(hechoService.obtenerHechosSinPaginar());
    }
}
