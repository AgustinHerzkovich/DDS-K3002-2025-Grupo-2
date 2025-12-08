package aplicacion.services;

import aplicacion.dto.input.IdentidadContribuyenteInputDto;
import aplicacion.dto.output.ContribuyenteOutputDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.*;

@Service
public class ContribuyenteService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final UsuarioService usuarioService;
    @Value("${keycloak.realm}")
    private String realm;

    @Value("${api.agregador.contribuyentes.register-if-not-found}")
    private Boolean registerIfNotFound;
    @Value("${keycloak.admin-client.id}")
    private String ADMIN_CLIENT_ID;

    @Value("${keycloak.admin-client.secret}")
    private String ADMIN_CLIENT_SECRET;
    private WebClient webClient;
    @Value("${api.publica.port}")
    private Integer apiPublicaPort;

    public ContribuyenteService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:" + apiPublicaPort + "/apiPublica")
                .build();
    }

    public ContribuyenteOutputDto obtenerContribuyentePorMail(OidcUser oidcUser) {
        try {
            // LO BUSCA EN AGREGADOR, NO VERIFICA QUE ESTE EN FUENTEDINAMICA
            String mail = oidcUser.getEmail();
            ContribuyenteOutputDto contribuyente = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/contribuyentes")
                            .queryParam("mail", mail)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ContribuyenteOutputDto>() {})
                    .block();

            if (contribuyente != null) {
                return contribuyente;
            } else {
                throw new RuntimeException("Error: El servidor respondio 200 OK, pero no devolvio el usuario");
            }
        } catch (WebClientResponseException e){

            if (e.getStatusCode() == HttpStatus.NOT_FOUND){
                if(registerIfNotFound){
                    System.out.println("Usuario no encontrado en GET ( "+oidcUser.getEmail()+" / "+oidcUser.getSubject()+" ) \n Registrando nuevamente...");
                    ContribuyenteOutputDto contribuyenteCreado = usuarioService.registrarUsuarioSiNoExiste(oidcUser);
                    return contribuyenteCreado;
                }else {
                    System.err.println("Usuario no encontrado en GET ( "+oidcUser.getEmail()+" / "+oidcUser.getSubject()+" ) \n Registro automático desactivado. Devolviendo usuario null.");
                }
            }

            throw e;
        }
        catch (Exception e) {
            System.err.println("Error al obtener contribuyente por mail: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public void actualizarIdentidad(String contribuyenteId, IdentidadContribuyenteInputDto identidadDto, OidcUser principal) throws HttpClientErrorException {


        try {
            String keycloakUserId = principal.getSubject();

            String adminToken = this.obtenerAdminToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String currentEmail = principal.getEmail();

            Map<String, Object> kcUpdateRequest = new HashMap<>();
            kcUpdateRequest.put("firstName", identidadDto.getNombre());
            kcUpdateRequest.put("lastName", identidadDto.getApellido());
            kcUpdateRequest.put("email", currentEmail);

            kcUpdateRequest.put("attributes", Collections.singletonMap(
                    "birthdate",
                    Collections.singletonList(identidadDto.getFechaNacimiento() != null ? identidadDto.getFechaNacimiento().toString() : "")
            ));

            HttpEntity<Map<String, Object>> kcRequest = new HttpEntity<>(kcUpdateRequest, headers);

            restTemplate.exchange(
                    "http://18.230.194.94:8888/admin/realms/metamapa/users/" + keycloakUserId,
                    HttpMethod.PUT,
                    kcRequest,
                    Void.class
            );
            System.out.println(" Keycloak actualizado para el usuario: " + keycloakUserId);

        } catch (HttpClientErrorException httpEx) {
            System.err.println(" ERROR Keycloak Admin Token: " + httpEx.getStatusCode());
            System.err.println(" Respuesta de Keycloak: " + httpEx.getResponseBodyAsString());
            throw new RuntimeException("Fallo al actualizar Keycloak. Código: " + httpEx.getStatusCode(), httpEx);
        } catch (Exception e) {
            throw new RuntimeException("Fallo de red o error inesperado al actualizar Keycloak.", e);
        }


        try {

            this.webClient.patch() // Usamos PATCH
                    .uri( "/contribuyentes/{id}/identidad", contribuyenteId)
                    .bodyValue(identidadDto)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

        } catch (org.springframework.web.reactive.function.client.WebClientResponseException webClientEx) {

            System.err.println("ERROR API de Identidad (" + webClientEx.getStatusCode() + "): " + webClientEx.getResponseBodyAsString());
            throw new HttpClientErrorException(webClientEx.getStatusCode(), webClientEx.getResponseBodyAsString());

        } catch (Exception e) {
            System.err.println("ERROR al procesar identidad: " + e.getMessage());
            throw new RuntimeException("Fallo de comunicación interna al actualizar la identidad.", e);
        }

    }

    private String obtenerAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        // La Admin API requiere el 'client_credentials' para obtener un token de servicio.
        map.add("grant_type", "client_credentials");
        map.add("client_id", ADMIN_CLIENT_ID);
        map.add("client_secret", ADMIN_CLIENT_SECRET);


        map.add("scope", "openid");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        String tokenUrl = "http://18.230.194.94:8888/realms/" + realm + "/protocol/openid-connect/token";

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    tokenUrl,
                    request,
                    Map.class
            );

            // Devolvemos el access_token
            return response.getBody().get("access_token").toString();

        } catch (Exception e) {
            System.err.println(" ERROR al obtener Admin Token de Keycloak: " + e.getMessage());
            throw new RuntimeException("Fallo al obtener el token de administrador.", e);
        }

    }
}

