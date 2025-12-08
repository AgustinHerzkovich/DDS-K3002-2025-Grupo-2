package aplicacion.controllers;
import aplicacion.config.TokenContext;
import aplicacion.dto.input.CambioEstadoRevisionInputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.services.HechoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;

@Controller
public class HechoController {
    private final HechoService hechoService;

    public HechoController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping("/hechos/{id}")
    public String paginaHecho(@PathVariable(name = "id") String id,
                             Model model) {
        TokenContext.addToken(model);

        HechoOutputDto hecho = hechoService.obtenerHecho(id);
        if (hecho == null) {
            return "error/404"; // Ver si está bien tirar esto o capaz convenga otra cosa
        }

        model.addAttribute("hecho", hecho);

        return "hecho";
    }
    @PostMapping("/gestionar-solicitud/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> gestionarSolicitud(
            @PathVariable(name = "id") String hechoId,
            @RequestBody CambioEstadoRevisionInputDto cambioEstadoDto, // DTO que contiene ESTADO y SUGERENCIA
            Model model
    ) {
        TokenContext.addToken(model);

        try {
            ResponseEntity<String> response = this.hechoService.gestionarRevision(hechoId, cambioEstadoDto);

            System.out.println("Gestión de solicitud enviada para ID: " + hechoId);

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException httpEx) {

            System.err.println(" ERROR API de Revisión (" + httpEx.getStatusCode() + "): " + httpEx.getResponseBodyAsString());
            return ResponseEntity.status(httpEx.getStatusCode()).body(httpEx.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("ERROR FATAL al gestionar hecho ID " + hechoId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Fallo de comunicación.\"}");
        }
    }

    @GetMapping("/hechos-pendientes")
    @PreAuthorize("hasRole('ADMIN')")
    public String showHechosPendientes(
            @AuthenticationPrincipal OidcUser oidcUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        TokenContext.addToken(model);

        model.addAttribute("principal", oidcUser);

        aplicacion.dto.PageWrapper<HechoOutputDto> hechosPendientesPage = hechoService.obtenerHechosPendientes(page, size);

        model.addAttribute("hechosPendientes", hechosPendientesPage.getContent());
        model.addAttribute("currentPage", hechosPendientesPage.getNumber());
        model.addAttribute("totalPages", hechosPendientesPage.getTotalPages());
        model.addAttribute("totalElements", hechosPendientesPage.getTotalElements());
        model.addAttribute("hasPrevious", !hechosPendientesPage.isFirst());
        model.addAttribute("hasNext", !hechosPendientesPage.isLast());

        return "hechos-pendientes";
    }
}
