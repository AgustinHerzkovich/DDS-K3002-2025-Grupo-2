package domain.peticiones;

import domain.context.RequestContext;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;

public class SolicitudesHttp {
    private final RestTemplate restTemplate;

    public SolicitudesHttp(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofMinutes(5))   // Timeout de conexión: 5 minutos
                .setReadTimeout(Duration.ofMinutes(50))     // Timeout de lectura: 50 minutos
                .build();
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //No tira error si llega 400, 500 o algo asi
            }
        });
    }

    // ------------------------
    // Metodo auxiliar para agregar Authorization automáticamente
    // ------------------------
    private HttpHeaders applyAuthorizationHeader(HttpHeaders headers) {
        if (headers == null) headers = new HttpHeaders();

        String token = RequestContext.getAuthToken();
        if (token != null) {
            headers.set(HttpHeaders.AUTHORIZATION, token);
        }

        return headers;
    }

    public <T> ResponseEntity<T> get(String path, Class<T> responseType) {
        HttpHeaders headers = applyAuthorizationHeader(new HttpHeaders());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<T> response = restTemplate.exchange(path, HttpMethod.GET, entity, responseType);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    public <T> ResponseEntity<T> get(String path, HttpHeaders headers, Class<T> responseType) {
        headers = applyAuthorizationHeader(headers);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<T> response = restTemplate.exchange(path, HttpMethod.GET, entity, responseType);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    public <T> ResponseEntity<T> post(String path, Object body, Class<T> responseType) {
        return post(path, body, responseType, null);
    }

    public <T> ResponseEntity<T> post(String path, Object body, Class<T> responseType, HttpHeaders headersExtra) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // aplicar Authorization
        headers = applyAuthorizationHeader(headers);

        // copiar otros headers extra
        if (headersExtra != null) {
            headersExtra.forEach(headers::addAll);
        }

        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        ResponseEntity<T> response = restTemplate.postForEntity(path, entity, responseType);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    public <T> ResponseEntity<T> delete(String path, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers = applyAuthorizationHeader(headers);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<T> response = restTemplate.exchange(path, HttpMethod.DELETE, entity, responseType);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    public <T> ResponseEntity<T> patch(String path, Object body, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers = applyAuthorizationHeader(headers);

        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        ResponseEntity<T> response = restTemplate.exchange(path, HttpMethod.PATCH, entity, responseType);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    public <T> ResponseEntity<T> postMultipart(String path, Object body, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers = applyAuthorizationHeader(headers);

        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        ResponseEntity<T> response = restTemplate.postForEntity(path, entity, responseType);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
