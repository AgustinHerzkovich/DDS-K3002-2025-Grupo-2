package aplicacion.controllers;

import aplicacion.services.SolicitudService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SolicitudController {
    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping("/solicitudes")
    public String paginaSolicitudes() {
        return "solicitudes"; // TODO
    }
}
