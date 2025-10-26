package aplicacion.controllers;

import aplicacion.dto.output.ContribuyenteOutputDto;
import aplicacion.dto.output.SolicitudOutputDto;
import aplicacion.services.ContribuyenteService;
import aplicacion.services.SolicitudService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SolicitudController {
    private final SolicitudService solicitudService;
    private final ContribuyenteService contribuyenteService;

    public SolicitudController(SolicitudService solicitudService, ContribuyenteService contribuyenteService) {
        this.solicitudService = solicitudService;
        this.contribuyenteService = contribuyenteService;
    }

    @GetMapping("/solicitudes")
    @PreAuthorize("hasRole('ADMIN')")
    public String paginaSolicitudes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @AuthenticationPrincipal OidcUser oidcUser,
            Model model) {

        List<SolicitudOutputDto> solicitudes = solicitudService.obtenerSolicitudes(page, size)
                .collectList()
                .block();

        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        return "solicitudes";
    }
}
