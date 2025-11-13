package aplicacion.controllers;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Controller
public class HechoController {
    private final HechoService hechoService;

    public HechoController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping("/hechos/{id}")
    public String paginaHecho(@PathVariable("id") String id,
                             Model model) {
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
            @PathVariable("id") String hechoId,
            @RequestBody CambioEstadoRevisionInputDto cambioEstadoDto // DTO que contiene ESTADO y SUGERENCIA
    ) {



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

    @GetMapping("/solicitudes-pendientes")
    @PreAuthorize("hasRole('ADMIN')")
    public String showSolicitudesPendientes(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        model.addAttribute("principal", oidcUser);


        model.addAttribute("solicitudes", java.util.Collections.emptyList());
        List<HechoOutputDto> solicitudes = hechoService.obtenerSolicitudesPendientes();


        model.addAttribute("solicitudes",solicitudes);
        return "solicitudes-pendientes";
    }
}
