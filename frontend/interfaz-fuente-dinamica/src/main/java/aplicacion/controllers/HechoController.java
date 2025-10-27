package aplicacion.controllers;

import aplicacion.dtos.input.CambioEstadoRevisionInputDto;
import aplicacion.dtos.input.HechoEdicionInputDto;
import aplicacion.dtos.input.HechoInputDto;
import aplicacion.dtos.output.HechoOutputDto;
import aplicacion.services.HechoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collection;
import java.util.List;

@Controller
public class HechoController {

    private final HechoService hechoService;

    public HechoController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping("/editar-hecho/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editarHechoForm(
            @PathVariable("id") String hechoId,
            @AuthenticationPrincipal OidcUser principal,
            Model model
    ) {
        // 1. Verificar autenticación y pasar principal
        if (principal == null) {
            return "redirect:/";
        }
        model.addAttribute("principal", principal);
        model.addAttribute("isAdmin", checkClaimForRole(principal, "admin"));

        HechoOutputDto hecho = this.hechoService.obtenerHechoPorId(hechoId);


        if (hecho != null) {

            model.addAttribute("hecho", hecho);
            return "editar-hecho"; // Carga la plantilla con los datos pre-llenados
        }

        // Si hay un error de API, hecho no existe, o no pertenece, redirigir a Mis Hechos
        return "redirect:/mis-hechos";
    }
    @PostMapping("/guardar-edicion/{id}")
    //@PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateHecho(
            @PathVariable("id") String hechoId,
            @RequestBody HechoEdicionInputDto hechoEdicionInputDto
    ) {
        System.out.println(" Iniciando edición para hecho ID: " + hechoId);

        try {
            ResponseEntity<String> response = this.hechoService.actualizarHecho(hechoId, hechoEdicionInputDto);

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException httpEx) {
            System.err.println("ERROR DE EDICIÓN (" + httpEx.getStatusCode() + "): " + httpEx.getResponseBodyAsString());
            return ResponseEntity.status(httpEx.getStatusCode()).body(httpEx.getResponseBodyAsString());

        } catch (Exception e) {
            System.err.println(" ERROR  al procesar la edición: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Fallo al comunicarse con el servicio de hechos.\"}");
        }
    }


    @PostMapping("/subir-hechos-post")
    public ResponseEntity<?> subirHechos(@RequestBody HechoInputDto hechoDto, HttpServletRequest request){

        Long idDeSesion = (Long) request.getSession().getAttribute("CONTRIBUYENTE_ID");

        // Forzar anonimato si el ID de sesión no existe.
        if (idDeSesion == null) {
            hechoDto.setAnonimato(true); // Forzar a anónimo
            hechoDto.setContribuyenteId(null);
            System.out.println(" Subida forzada a anónima (usuario no logueado).");

        } else if (!hechoDto.getAnonimato()) {
            //  CASO LOGUEADO Y NO ANÓNIMO
            // Si el usuario está logueado (tiene ID) y no marcó anonimato, se vincula.
            hechoDto.setContribuyenteId(idDeSesion);
            System.out.println(" Hecho vinculado al ID: " + idDeSesion);

        } else {
            // CASO LOGUEADO Y ANÓNIMO
            // Si está logueado pero marcó anonimato, se acepta la petición anónima.
            hechoDto.setContribuyenteId(null);
            System.out.println(" Subida anónima solicitada por usuario logueado.");
        }

        // Delegación y manejo de errores (El servicio no necesita cambios)
        try {
            ResponseEntity<String> response = hechoService.crearHecho(hechoDto);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (Exception e) {
            System.err.println(" ERROR FATAL al procesar hecho: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Comunicación fallida.\"}");
        }
    }


    @GetMapping("/solicitudes-pendientes")
    @PreAuthorize("hasRole('ADMIN')")
    public String showSolicitudesPendientes(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        model.addAttribute("principal", oidcUser);
        boolean isAdmin = checkClaimForRole(oidcUser, "admin");
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("solicitudes", java.util.Collections.emptyList());
        List<HechoOutputDto> solicitudes = hechoService.obtenerSolicitudesPendientes();


        model.addAttribute("solicitudes",solicitudes);
        return "solicitudes-pendientes";
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

