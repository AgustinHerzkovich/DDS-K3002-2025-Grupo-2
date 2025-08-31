package domain.peticiones;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class SolicitudesHttp {
    private final RestTemplate restTemplate;

    public SolicitudesHttp(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
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

    public <T> ResponseEntity<T> get(String path, Class<T> responseType) {
        ResponseEntity<T> response = restTemplate.getForEntity(path, responseType);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    public <T> ResponseEntity<T> post(String path, Object body, Class<T> responseType) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<>(body, headers);
            ResponseEntity<T> response = restTemplate.postForEntity(path, entity, responseType);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

    }

    public <T> ResponseEntity<T> delete(String path, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<T> response = restTemplate.exchange(path, HttpMethod.DELETE, entity, responseType);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    public <T> ResponseEntity<T> patch(String path, Object body, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        ResponseEntity<T> response = restTemplate.exchange(path, HttpMethod.PATCH, entity, responseType);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
