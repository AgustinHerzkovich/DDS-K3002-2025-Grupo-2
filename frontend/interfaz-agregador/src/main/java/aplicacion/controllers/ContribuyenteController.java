// java
package aplicacion.controllers;

import aplicacion.dto.input.IdentidadContribuyenteInputDto;
import aplicacion.services.ContribuyenteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;

@Controller
public class ContribuyenteController {

    private final ContribuyenteService contribuyenteService;

    public ContribuyenteController(ContribuyenteService contribuyenteService) {
        this.contribuyenteService = contribuyenteService;
    }
    /*
    @GetMapping("/editar-perfil")
    @PreAuthorize("isAuthenticated()")
    public String editarPerfilForm(@AuthenticationPrincipal OidcUser principal, Model model) {
        if (principal != null) {
            model.addAttribute("principal", principal);

            Map<String, Object> claims = principal.getClaims();
            Map<String, String> perfilUsuario = new HashMap<>();

            perfilUsuario.put("nombre", (String) claims.get("given_name"));
            perfilUsuario.put("apellido", (String) claims.get("family_name"));

            String fechaNacimientoClaim = principal.getClaimAsString("birthdate");
            perfilUsuario.put("fechaNacimiento", fechaNacimientoClaim);

            perfilUsuario.put("email", principal.getEmail());

            model.addAttribute("usuario", perfilUsuario);

            return "editar-perfil";
        }
        return "redirect:/";
    }*/

    @PostMapping("/editarIdentidad")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> modificarIdentidad(
            @RequestBody IdentidadContribuyenteInputDto identidadContribuyenteInputDto,
            HttpServletRequest request,
            @AuthenticationPrincipal OidcUser principal
    ) {
        String contribuyenteId = (String) request.getSession().getAttribute("CONTRIBUYENTE_ID");

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


}