package domain.peticiones;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SolicitudesHttp {
    private final RestTemplate restTemplate;

    public SolicitudesHttp(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public <T> ResponseEntity<T> get(String path, Class<T> responseType) {
        try {
            return ResponseEntity.ok(restTemplate.getForObject(path, responseType));
        }catch (Exception e) {
            System.out.println("Error en la solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(null);
        }
    }

    public <T> ResponseEntity<T> post(String path, Object body, Class<T> responseType) {
        try{
            return restTemplate.postForEntity(path, body, responseType);
        }catch (Exception e) {
            System.out.println("Error en la solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(null);
        }
    }

    public <T> ResponseEntity<T> delete(String path) {
        try {
            restTemplate.delete(path);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            System.out.println("Error en la solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(null);
        }
    }

    public <T> ResponseEntity<T> patch(String path, Object body) {
        try {
            restTemplate.patchForObject(path, body, Void.class);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            System.out.println("Error en la solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(null);
        }
    }
}
