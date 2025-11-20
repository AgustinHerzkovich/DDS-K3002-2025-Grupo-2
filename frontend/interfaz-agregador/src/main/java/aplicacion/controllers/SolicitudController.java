package aplicacion.controllers;

import aplicacion.dto.PageWrapper;
import aplicacion.dto.output.SolicitudOutputDto;
import aplicacion.services.ContribuyenteService;
import aplicacion.services.SolicitudService;
import org.springframework.security.access.prepost.PreAuthorize;
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
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "3") Integer size,
            Model model) {

        PageWrapper<SolicitudOutputDto> pageWrapper = solicitudService.obtenerSolicitudes(page, size)
                .block();

        if (pageWrapper == null) {
            model.addAttribute("solicitudes", List.of());
            model.addAttribute("currentPage", 0);
            model.addAttribute("pageSize", size);
            model.addAttribute("hasNext", false);
            model.addAttribute("hasPrevious", false);
            model.addAttribute("totalPages", 0);
            return "solicitudes";
        }

        model.addAttribute("solicitudes", pageWrapper.getContent());
        model.addAttribute("currentPage", pageWrapper.getNumber());
        model.addAttribute("pageSize", pageWrapper.getSize());
        model.addAttribute("hasNext", !pageWrapper.isLast());
        model.addAttribute("hasPrevious", !pageWrapper.isFirst());
        model.addAttribute("totalPages", pageWrapper.getTotalPages());

        return "solicitudes";
    }
}
