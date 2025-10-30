package aplicacion.controllers;

import aplicacion.dtos.input.IdentidadContribuyenteInputDto;
import aplicacion.dtos.output.HechoOutputDto;
import aplicacion.services.ContribuyenteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collection;
import java.util.List;


@Controller
public class ContribuyenteController {

    private final ContribuyenteService contribuyenteService;

    public ContribuyenteController(ContribuyenteService contribuyenteService) {
        this.contribuyenteService = contribuyenteService;
    }

    @PostMapping("/editarIdentidad")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> modificarIdentidad(
            @RequestBody IdentidadContribuyenteInputDto identidadContribuyenteInputDto,
            HttpServletRequest request,
            @AuthenticationPrincipal OidcUser principal
    ) {
        Long contribuyenteId = (Long) request.getSession().getAttribute("CONTRIBUYENTE_ID");

        if (contribuyenteId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\": \"ID de contribuyente no encontrado. Vuelva a iniciar sesión.\"}");
        }

        try {
            // Llama al servicio que contiene la lógica de negocio y APIs
            this.contribuyenteService.actualizarIdentidad(contribuyenteId, identidadContribuyenteInputDto, principal);

            // La lógica de éxito (invalidación de sesión y 204) permanece aquí.
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            System.out.println("Sesión invalidada. Devolviendo 204 para que el frontend redirija.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (HttpClientErrorException httpEx) {
            // Captura de errores 4xx/5xx específicos del microservicio (propagados desde el servicio)
            System.err.println("ERROR API de Identidad (" + httpEx.getStatusCode() + "): " + httpEx.getResponseBodyAsString());
            return ResponseEntity.status(httpEx.getStatusCode()).body(httpEx.getResponseBodyAsString());
        } catch (RuntimeException e) {
            // Captura de errores generales (incluyendo fallos de Keycloak Admin API)
            System.err.println("ERROR FATAL al procesar identidad: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Fallo en el servicio o comunicación interna.\"}");
        }
    }


    @GetMapping("/mis-hechos")
    @PreAuthorize("isAuthenticated()") // Asegura que solo usuarios logueados accedan
    public String misHechos(@AuthenticationPrincipal OidcUser principal, Model model, HttpServletRequest request) {

        // Obtener el ID del Contribuyente de la sesión
        Long contribuyenteId = (Long) request.getSession().getAttribute("CONTRIBUYENTE_ID");

        model.addAttribute("principal", principal);
        boolean isAdmin = checkClaimForRole(principal, "admin");
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("hechos", java.util.Collections.emptyList()); // Inicializa la lista por si falla

        if (contribuyenteId == null) {
            System.err.println(" ERROR: Contribuyente ID no encontrado en la sesión. Redirigiendo a inicio.");
            return "mis-hechos";
        }
        List<HechoOutputDto> hechos = this.contribuyenteService.obtenerHechosPorContribuyente(contribuyenteId);
        model.addAttribute("hechos", hechos);


        return "mis-hechos";
    }
    private boolean checkClaimForRole(OidcUser oidcUser, String targetRole) {
        // 1. Obtener el claim "realm_roles" (donde Keycloak pone el rol)
        Object rolesClaim = oidcUser.getClaim("realm_roles");

        if (rolesClaim instanceof Collection<?> roles) {
            // 2. Comprobar si la lista contiene el rol (ignorando caso y prefijo)
            return roles.stream()
                    .map(Object::toString)
                    .anyMatch(role -> role.equalsIgnoreCase(targetRole));
        }
        return false;
    }

}
