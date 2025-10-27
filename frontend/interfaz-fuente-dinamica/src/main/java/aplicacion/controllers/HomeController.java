package aplicacion.controllers;


import aplicacion.dtos.input.CambioEstadoRevisionInputDto;
import aplicacion.dtos.input.HechoEdicionInputDto;
import aplicacion.dtos.input.HechoInputDto;
import aplicacion.dtos.input.IdentidadContribuyenteInputDto;
import aplicacion.dtos.output.HechoOutputDto;
import jakarta.servlet.http.HttpServletRequest;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.*;


@Controller
public class HomeController {
    private final RestTemplate restTemplate;
    //@Value("${fuente.dinamica.port}")
    //private static String fuenteDinamicaPort;
    private static final String HECHOS_API_URL = "http://localhost:8082/fuentesDinamicas";

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

    // Metodo de utilidad para verificar el claim
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

            boolean isAdmin = checkClaimForRole(principal, "admin");
            model.addAttribute("isAdmin", isAdmin);

            model.addAttribute("usuario", perfilUsuario);

            return "editar-perfil";
        }
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
            String url = HECHOS_API_URL+"/hechos";

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
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
            // Si el ID falta, no podemos obtener los hechos. Podrías redirigir o mostrar un mensaje.
            return "mis-hechos";
        }

        String url = HECHOS_API_URL + "/contribuyentes/"+contribuyenteId + "/hechos";

        try {
            // Usamos ParameterizedTypeReference para manejar la lista genérica
            ResponseEntity<List<HechoOutputDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new org.springframework.core.ParameterizedTypeReference<List<HechoOutputDto>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                //  Pasar la lista de hechos al modelo para que Thymeleaf la use
                model.addAttribute("hechos", response.getBody());
                System.out.println("ÉXITO: Cargados " + response.getBody().size() + " hechos para el contribuyente ID: " + contribuyenteId);
            } else {
                System.err.println("API DE HECHOS devolvió estado: " + response.getStatusCode());
            }

        } catch (Exception e) {
            System.err.println("ERROR al obtener hechos del contribuyente: " + e.getMessage());

        }

        return "mis-hechos";
    }

    @GetMapping("/solicitudes-pendientes")
    @PreAuthorize("hasRole('ADMIN')")
    public String showSolicitudesPendientes(@AuthenticationPrincipal OidcUser oidcUser, Model model) {
        model.addAttribute("principal", oidcUser);
        boolean isAdmin = checkClaimForRole(oidcUser, "admin");
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("solicitudes", java.util.Collections.emptyList());

        // 1. URL para obtener hechos pendientes (pendiente=false)
        String apiUrl = HECHOS_API_URL + "/hechos?pendiente=true";

        try {
            // Usamos ParameterizedTypeReference para manejar la lista genérica
            ResponseEntity<List<HechoOutputDto>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new org.springframework.core.ParameterizedTypeReference<List<HechoOutputDto>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                model.addAttribute("solicitudes", response.getBody());
                System.out.println(" Cargadas " + response.getBody().size() + " solicitudes pendientes.");
            } else {
                model.addAttribute("solicitudes", java.util.Collections.emptyList());
            }

        } catch (Exception e) {
            System.err.println("ERROR al obtener solicitudes pendientes: " + e.getMessage());
            model.addAttribute("solicitudes", java.util.Collections.emptyList());
        }

        return "solicitudes-pendientes";
    }

    @GetMapping("/check-roles")
    @ResponseBody
    public String checkRoles(@AuthenticationPrincipal OidcUser principal) {
        StringBuilder sb = new StringBuilder();
        sb.append("Roles del usuario:\n");
        for (GrantedAuthority auth : principal.getAuthorities()) {
            sb.append(auth.getAuthority()).append("\n");
        }
        return sb.toString();
    }

    @PostMapping("/guardar-edicion/{id}")
    //@PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateHecho(
            @PathVariable("id") String hechoId,
            @RequestBody HechoEdicionInputDto hechoEdicionInputDto
    ) {
        System.out.println(" Iniciando edición para hecho ID: " + hechoId);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<HechoEdicionInputDto> requestEntity = new HttpEntity<>(hechoEdicionInputDto, headers);

            String apiUrl = HECHOS_API_URL +"/hechos/"+ hechoId;

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.PATCH,
                    requestEntity,
                    String.class
            );

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException httpEx) {
            System.err.println("ERROR DE EDICIÓN (" + httpEx.getStatusCode() + "): " + httpEx.getResponseBodyAsString());
            return ResponseEntity.status(httpEx.getStatusCode()).body(httpEx.getResponseBodyAsString());

        } catch (Exception e) {
            System.err.println(" ERROR  al procesar la edición: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Fallo al comunicarse con el servicio de hechos.\"}");
        }
    }
    @GetMapping("/editar-hecho/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editarHechoForm(
            @PathVariable("id") String hechoId,
            @AuthenticationPrincipal OidcUser principal,
            Model model,
            HttpServletRequest request
    ) {
        // 1. Verificar autenticación y pasar principal
        if (principal == null) {
            return "redirect:/";
        }
        model.addAttribute("principal", principal);
        model.addAttribute("isAdmin", checkClaimForRole(principal, "admin"));



        // 3. Llamada al microservicio para obtener los datos del hecho
        String apiUrl = HECHOS_API_URL +"/hechos/"+ hechoId;

        try {
            // Obtenemos los datos actuales del hecho
            ResponseEntity<HechoOutputDto> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    HechoOutputDto.class // DTO que trae todos los datos
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                HechoOutputDto hecho = response.getBody();


                // 4. Pasar el objeto Hecho completo al modelo
                model.addAttribute("hecho", hecho);

                return "editar-hecho"; // Carga la plantilla con los datos pre-llenados
            }

        } catch (Exception e) {
            System.err.println(" ERROR al obtener hecho ID " + hechoId + ": " + e.getMessage());
        }

        // Si hay un error de API, hecho no existe, o no pertenece, redirigir a Mis Hechos
        return "redirect:/mis-hechos";
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

        String apiUrl = HECHOS_API_URL+"/contribuyentes/" + contribuyenteId + "/identidad";
        try {
            String keycloakUserId = principal.getSubject();
            String adminToken = obtenerAdminToken(); // Asume que este método existe y funciona

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Crear el cuerpo de la petición. Keycloak Admin API usa 'firstName', 'lastName' y 'attributes'
            Map<String, Object> kcUpdateRequest = new HashMap<>();
            kcUpdateRequest.put("firstName", identidadContribuyenteInputDto.getNombre());
            kcUpdateRequest.put("lastName", identidadContribuyenteInputDto.getApellido());

            // Manejo del atributo de fecha de nacimiento (birthdate)
            // Se envía la fecha como una lista de String dentro del mapa de atributos
            kcUpdateRequest.put("attributes", Collections.singletonMap(
                    "birthdate",
                    Collections.singletonList(identidadContribuyenteInputDto.getFechaNacimiento().toString())
            ));

            HttpEntity<Map<String, Object>> kcRequest = new HttpEntity<>(kcUpdateRequest, headers);

            // Ejecutar PUT a la Admin API de Keycloak para actualizar el usuario
            restTemplate.exchange(
                    "http://localhost:8888/admin/realms/metamapa/users/" + keycloakUserId,
                    HttpMethod.PUT,
                    kcRequest,
                    Void.class // No esperamos cuerpo de respuesta
            );
            System.out.println(" Keycloak actualizado para el usuario: " + keycloakUserId);

        } catch (HttpClientErrorException kcEx) {
            // Capturar errores 400/409/500 de la Admin API
            System.err.println(" ERROR al actualizar Keycloak: " + kcEx.getResponseBodyAsString());
            return ResponseEntity.status(kcEx.getStatusCode()).body(kcEx.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Fallo al actualizar Keycloak: " + e.getMessage() + "\"}");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<IdentidadContribuyenteInputDto> requestEntity = new HttpEntity<>(identidadContribuyenteInputDto, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.PATCH,
                    requestEntity,
                    String.class
            );

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpClientErrorException httpEx) {
            // Capturar y reenviar 400 o 404 del microservicio
            System.err.println("ERROR API de Identidad (" + httpEx.getStatusCode() + "): " + httpEx.getResponseBodyAsString());
            return ResponseEntity.status(httpEx.getStatusCode()).body(httpEx.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("ERROR  al procesar identidad: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Fallo de comunicación interna.\"}");
        }

    }

    @PostMapping("/gestionar-solicitud/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> gestionarSolicitud(
            @PathVariable("id") String hechoId,
            @RequestBody CambioEstadoRevisionInputDto cambioEstadoDto // DTO que contiene ESTADO y SUGERENCIA
    ) {

        //  http://localhost:8082/fuentesDinamicas/hechos/{id}/estadoRevision
        final String MICROSERVICE_URL = HECHOS_API_URL + "/hechos/" + hechoId + "/estadoRevision";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CambioEstadoRevisionInputDto> requestEntity = new HttpEntity<>(cambioEstadoDto, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    MICROSERVICE_URL,
                    HttpMethod.PATCH,
                    requestEntity,
                    String.class
            );

            System.out.println("Gestión de solicitud enviada para ID: " + hechoId);

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (org.springframework.web.client.HttpClientErrorException httpEx) {
            // Capturar 404 (Hecho no encontrado) u otros errores de negocio
            System.err.println(" ERROR API de Revisión (" + httpEx.getStatusCode() + "): " + httpEx.getResponseBodyAsString());
            return ResponseEntity.status(httpEx.getStatusCode()).body(httpEx.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("ERROR FATAL al gestionar hecho ID " + hechoId + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Fallo de comunicación.\"}");
        }

    }
}




