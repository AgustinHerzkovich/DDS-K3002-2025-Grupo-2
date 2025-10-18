package aplicacion.controllers;


import aplicacion.dtos.HechoInputDto;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    private final RestTemplate restTemplate;

    public HomeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @GetMapping({"/", "/home"})
    public String paginaInicial(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        if (oidcUser != null) {
            model.addAttribute("principal", oidcUser);


            boolean isAdmin = checkClaimForRole(oidcUser, "admin"); // Chequeamos el rol sin prefijo
            model.addAttribute("isAdmin", isAdmin);

        }
        return "homepage";
    }

    // Creamos un método de utilidad para verificar el claim
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

    // Endpoint para solicitudes pendientes (ÚNICA DEFINICIÓN)
    @GetMapping("/solicitudes-pendientes")
    public String showSolicitudesPendientes(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        if (oidcUser != null) {
            model.addAttribute("principal", oidcUser);

        }
        return "solicitudes-pendientes";
    }


    @GetMapping("/editar-perfil")
    public String editarPerfilForm(@AuthenticationPrincipal OidcUser principal, Model model) {
        if (principal != null) {
            // Asegúrate de pasar 'principal' al modelo para los fragmentos de layout
            model.addAttribute("principal", principal);

            Map<String, Object> claims = principal.getClaims();
            Map<String, String> perfilUsuario = new HashMap<>();
            perfilUsuario.put("nombre", (String) claims.get("given_name"));
            perfilUsuario.put("apellido", (String) claims.get("family_name"));
            perfilUsuario.put("email", principal.getEmail());
            model.addAttribute("usuario", perfilUsuario);

            return "editar-perfil";
        }
        // Redirigir si no está autenticado
        return "redirect:/";
    }


    @GetMapping("/subir-hechos")
    public String showUploadForm(@AuthenticationPrincipal OidcUser principal, Model model) {
        if (principal != null) {
            model.addAttribute("principal", principal);

            boolean isAdmin = checkClaimForRole(principal, "admin");
            model.addAttribute("isAdmin", isAdmin);
        }
        return "subir-hechos";
    }

    @PostMapping("/save-redirect-url")
    @ResponseBody
    public String saveRedirectUrl(@RequestParam String url, HttpServletRequest request) {
        request.getSession().setAttribute("REDIRECT_URL_AFTER_LOGIN", url);
        return "OK";
    }

    private static final String HECHOS_API_URL = "http://localhost:8082/fuentesDinamicas/hechos";

    @PostMapping("/subir-hechos-post")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> subirHechos(@RequestBody HechoInputDto hechoDto, HttpServletRequest request) {

        Long idDeSesion = (Long) request.getSession().getAttribute("CONTRIBUYENTE_ID");

        // 1. Lógica para la vinculación (Si NO es anónimo)
        if (!hechoDto.getAnonimato()) {

            if (idDeSesion == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"error\": \"ID de contribuyente no encontrado en la sesión. Vuelva a iniciar sesión.\"}");
            }

            //  ÚNICA INSERCIÓN NECESARIA: ID en la raíz
            hechoDto.setContribuyenteId(idDeSesion);
            System.out.println(" Hecho vinculado al ID: " + idDeSesion);

        } else {
            // 2. Si es anónimo, se asegura que el campo sea NULL
            hechoDto.setContribuyenteId(null);
            System.out.println(" Subida anónima.");
        }

        // 3. Enviar el DTO al microservicio (POST final)
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<HechoInputDto> requestEntity = new HttpEntity<>(hechoDto, headers);

            // POST al microservicio
            ResponseEntity<String> response = restTemplate.exchange(
                    HECHOS_API_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // Retornar la respuesta del microservicio
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (Exception e) {
            // Manejo de errores
            System.err.println(" ERROR FATAL al procesar hecho: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Comunicación fallida.\"}");
        }
    }

    /*
    @PostMapping("/registrar")
    @ResponseBody
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroUsuarioDto registroDto) {
        try {
            // 1. Obtener token de admin
            String adminToken = obtenerAdminToken();

            // 2. Crear el usuario en Keycloak
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> userRepresentation = new HashMap<>();
            userRepresentation.put("username", registroDto.getEmail());
            userRepresentation.put("email", registroDto.getEmail());
            userRepresentation.put("enabled", true);
            userRepresentation.put("firstName", registroDto.getNombre());
            userRepresentation.put("lastName", registroDto.getApellido());

            // Credenciales
            Map<String, Object> credentials = new HashMap<>();
            credentials.put("type", "password");
            credentials.put("value", registroDto.getPassword());
            credentials.put("temporary", false);
            userRepresentation.put("credentials", List.of(credentials));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(userRepresentation, headers);

            restTemplate.postForEntity(
                    keycloakServerUrl + "/admin/realms/" + realm + "/users",
                    request,
                    String.class
            );

            return ResponseEntity.ok().body("Usuario registrado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar usuario: " + e.getMessage());
        }
    }

    private String obtenerAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "metamapa-admin-client");
        map.add("client_secret", "uSC3EQ3PxezV455tqkHwz3pr36b7GTX8");
        map.add("grant_type", "password");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                keycloakServerUrl + "/realms/master/protocol/openid-connect/token",
                request,
                Map.class
        );

        return response.getBody().get("access_token").toString();
    }
    */

}