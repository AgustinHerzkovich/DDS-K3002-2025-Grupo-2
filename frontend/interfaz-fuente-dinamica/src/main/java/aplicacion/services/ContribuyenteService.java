package aplicacion.services;

import aplicacion.dtos.input.IdentidadContribuyenteInputDto;
import aplicacion.dtos.output.HechoOutputDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ContribuyenteService {
    private final RestTemplate restTemplate;
    private final String CONTRIBUYENTE_URL = "http://localhost:8082/fuentesDinamicas/contribuyentes";
    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin-client.id}")
    private String ADMIN_CLIENT_ID;

    @Value("${keycloak.admin-client.secret}")
    private String ADMIN_CLIENT_SECRET;

    public ContribuyenteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public void actualizarIdentidad(Long contribuyenteId, IdentidadContribuyenteInputDto identidadDto, OidcUser principal) throws HttpClientErrorException {

        String apiUrl = CONTRIBUYENTE_URL + "/" + contribuyenteId + "/identidad";

        // ==. Actualización en Keycloak ===
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
                    Collections.singletonList(identidadDto.getFechaNacimiento().toString())
            ));

            HttpEntity<Map<String, Object>> kcRequest = new HttpEntity<>(kcUpdateRequest, headers);

            restTemplate.exchange(
                    "http://localhost:8888/admin/realms/metamapa/users/" + keycloakUserId,
                    HttpMethod.PUT,
                    kcRequest,
                    Void.class
            );
            System.out.println(" Keycloak actualizado para el usuario: " + keycloakUserId);

        } catch (HttpClientErrorException httpEx) {
            System.err.println(" ERROR Keycloak Admin Token: " + httpEx.getStatusCode());
            System.err.println(" Respuesta de Keycloak: " + httpEx.getResponseBodyAsString());
            // Se propaga la excepción, el controlador la capturará
            throw new RuntimeException("Fallo al actualizar Keycloak. Código: " + httpEx.getStatusCode(), httpEx);
        } catch (Exception e) {
            throw new RuntimeException("Fallo de red o error inesperado al actualizar Keycloak.", e);
        }

        // ===  Actualización en backend
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<IdentidadContribuyenteInputDto> requestEntity = new HttpEntity<>(identidadDto, headers);

            restTemplate.exchange(
                    apiUrl,
                    HttpMethod.PATCH,
                    requestEntity,
                    String.class // Se usa String para capturar el cuerpo en caso de error 
            );

        } catch (HttpClientErrorException httpEx) {
            System.err.println("ERROR API de Identidad (" + httpEx.getStatusCode() + "): " + httpEx.getResponseBodyAsString());
            // Convertimos el error 4xx o 5xx en una excepción para que el controlador lo maneje
            throw new HttpClientErrorException(httpEx.getStatusCode(), httpEx.getResponseBodyAsString());
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

        String tokenUrl = "http://localhost:8888/realms/" + realm + "/protocol/openid-connect/token";

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

    public List<HechoOutputDto> obtenerHechosPorContribuyente(Long contribuyenteId) {
        String url = CONTRIBUYENTE_URL + "/" + contribuyenteId + "/hechos";

        try {
            // Usamos ParameterizedTypeReference para manejar la lista genérica
            ResponseEntity<List<HechoOutputDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new org.springframework.core.ParameterizedTypeReference<List<HechoOutputDto>>() {
                    }
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                System.out.println("ÉXITO: Cargados " + response.getBody().size() + " hechos para el contribuyente ID: " + contribuyenteId);
                return response.getBody();
            } else {
                System.err.println("API DE HECHOS devolvió estado: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("ERROR al obtener hechos del contribuyente: " + e.getMessage());
        }
        return Collections.emptyList();
    }
}


