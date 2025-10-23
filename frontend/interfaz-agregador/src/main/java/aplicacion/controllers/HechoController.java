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

        // Agregar ID del contribuyente de la BD si está logueado
        if (oidcUser != null) {
            // Obtener el email del usuario de Keycloak
            String email = oidcUser.getEmail();
            System.out.println("Email del usuario de Keycloak: " + email);

            // Buscar el contribuyente en la BD por email
            ContribuyenteOutputDto contribuyente = contribuyenteService.obtenerContribuyentePorMail(email);

            if (contribuyente != null) {
                // Usar el ID del contribuyente de la BD
                System.out.println("ID del contribuyente encontrado: " + contribuyente.getId());
                model.addAttribute("userId", contribuyente.getId());
            } else {
                System.err.println("No se encontró contribuyente con el email: " + email);
            }
        } else {
            System.err.println("OidcUser es null - usuario no autenticado");
        }

        return "hecho";
    }
}
