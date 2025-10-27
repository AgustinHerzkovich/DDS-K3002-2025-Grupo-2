package aplicacion.services;

import aplicacion.dtos.input.CambioEstadoRevisionInputDto;
import aplicacion.dtos.input.HechoEdicionInputDto;
import aplicacion.dtos.input.HechoInputDto;
import aplicacion.dtos.output.HechoOutputDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class HechoService {
    private final String FUENTE_DINAMICA_URL = "http://localhost:8082/fuentesDinamicas/hechos";
    private final RestTemplate restTemplate;

    public HechoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HechoOutputDto obtenerHechoPorId(String hechoId) {

        String apiUrl = FUENTE_DINAMICA_URL +"/"+ hechoId;

        try {
            // Obtenemos los datos actuales del hecho
            ResponseEntity<HechoOutputDto> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    HechoOutputDto.class // DTO que trae todos los datos
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();

            }
        } catch (Exception e) {
            System.err.println(" ERROR al obtener hecho ID " + hechoId + ": " + e.getMessage());
        }
        return null;
    }

    public ResponseEntity<String> actualizarHecho(String hechoId, HechoEdicionInputDto hechoEdicionInputDto) throws HttpClientErrorException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<HechoEdicionInputDto> requestEntity = new HttpEntity<>(hechoEdicionInputDto, headers);

        String apiUrl = FUENTE_DINAMICA_URL + "/" + hechoId;

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.PATCH,
                requestEntity,
                String.class
        );

        return response;

    }
    public ResponseEntity<String> crearHecho(HechoInputDto  hechoInputDto) throws HttpClientErrorException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<HechoInputDto> requestEntity = new HttpEntity<>(hechoInputDto, headers);
        String url = FUENTE_DINAMICA_URL;
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        return response ;
    }

    public List<HechoOutputDto> obtenerSolicitudesPendientes() {
        String apiUrl = FUENTE_DINAMICA_URL + "?pendiente=true";

        try {
            // Se usa ParameterizedTypeReference para manejar la des-serialización de List<HechoOutputDto>
            ResponseEntity<List<HechoOutputDto>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new org.springframework.core.ParameterizedTypeReference<List<HechoOutputDto>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                System.out.println(" Servicio: Cargadas " + response.getBody().size() + " solicitudes pendientes.");
                return response.getBody();
            }

        } catch (Exception e) {
            System.err.println(" ERROR en el servicio al obtener solicitudes pendientes: " + e.getMessage());

        }
        //devuelve lsita vacia si no hay
        return Collections.emptyList();
    }

    public ResponseEntity<String> gestionarRevision(String hechoId, CambioEstadoRevisionInputDto cambioEstadoRevisionInputDto) throws HttpClientErrorException{
        String url= FUENTE_DINAMICA_URL+"/" + hechoId + "/estadoRevision";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CambioEstadoRevisionInputDto> requestEntity = new HttpEntity<>(cambioEstadoRevisionInputDto, headers);

        // El manejo de HttpClientErrorException (errores 4xx) se delega al Controller
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                requestEntity,
                String.class
        );

        return response;
    }
}


