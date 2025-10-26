package aplicacion.controllers;

import aplicacion.dto.output.ContribuyenteOutputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.services.ContribuyenteService;
import aplicacion.services.HechoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HechoController {
    private final HechoService hechoService;
    private final ContribuyenteService contribuyenteService;

    public HechoController(HechoService hechoService, ContribuyenteService contribuyenteService) {
        this.hechoService = hechoService;
        this.contribuyenteService = contribuyenteService;
    }

    @GetMapping("/hechos/{id}")
    public String paginaHecho(@PathVariable("id") String id,
                             @AuthenticationPrincipal OidcUser oidcUser,
                             Model model) {
        HechoOutputDto hecho = hechoService.obtenerHecho(id);
        model.addAttribute("hecho", hecho);

        return "hecho";
    }
}
