package aplicacion.services;

import aplicacion.dtos.input.ContribuyenteInputDto;
import aplicacion.dtos.input.IdentidadContribuyenteInputDto;
import aplicacion.dtos.output.ContribuyenteOutputDto;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDate;
import java.util.Collection;

@Service
public class UsuarioService {

    private final String contribuyenteUrl= "http://localhost:8082/fuentesDinamicas/contribuyentes";
    private final RestTemplate restTemplate;

    public UsuarioService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

    }


    public void registrarUsuarioSiNoExiste(OidcUser oidcUser, HttpServletRequest request) {
        IdentidadContribuyenteInputDto identidad = new IdentidadContribuyenteInputDto(oidcUser.getClaimAsString("given_name"),
                                                                                      oidcUser.getClaimAsString("family_name"),
                                                                                      LocalDate.parse(oidcUser.getClaimAsString("birthdate")));
        boolean esAdmin = false;
        Object rolesClaim = oidcUser.getClaim("realm_roles");
        if (rolesClaim instanceof Collection<?> roles) {
            esAdmin = roles.stream()
                    .map(Object::toString)
                    .anyMatch(role -> role.equalsIgnoreCase("admin"));
        }

        ContribuyenteInputDto contribuyente = new ContribuyenteInputDto(esAdmin, identidad, oidcUser.getEmail());

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ContribuyenteInputDto> requestEntity = new HttpEntity<>(contribuyente, headers);
            // 2. Ejecutar POST y capturar la respuesta (que contiene el ID)
            ResponseEntity<ContribuyenteOutputDto> response = restTemplate.exchange(
                    contribuyenteUrl,
                    HttpMethod.POST,
                    requestEntity,
                    ContribuyenteOutputDto.class // Captura el DTO de salida
            );
            // Verificar éxito y guardar ID
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Long contribuyenteId = response.getBody().getId();
                //  Guardar el ID en la Sesión HTTP para usarlo después (ej. para subir hechos)
                request.getSession().setAttribute("CONTRIBUYENTE_ID", contribuyenteId);
                System.out.println("Contribuyente ID guardado en sesión: " + contribuyenteId);
            }
        } catch (Exception e) {
            System.err.println("ERROR al enviar POST para crear Contribuyente: " + e.getMessage());
        }
    }
}

