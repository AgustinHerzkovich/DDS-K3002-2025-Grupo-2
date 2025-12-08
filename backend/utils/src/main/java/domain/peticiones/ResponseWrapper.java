package domain.peticiones;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * Clase auxiliar para manejar respuestas de servicios externos que pueden devolver
 * tanto JSON como texto plano (especialmente en casos de error).
 *
 * Convierte respuestas de texto plano en JSON con formato consistente:
 * {"error": "mensaje de error"}
 */
public class ResponseWrapper {

    /**
     * Envuelve una respuesta String en un ResponseEntity con manejo inteligente de JSON/texto plano.
     *
     * - Si la respuesta es exitosa (2xx): devuelve el JSON tal cual
     * - Si es error (4xx/5xx) y es texto plano: lo convierte a {"error": "mensaje"}
     * - Si es error (4xx/5xx) y ya es JSON: lo devuelve como está
     *
     * @param response ResponseEntity con body tipo String
     * @return ResponseEntity con body procesado
     */
    public static ResponseEntity<?> wrapResponse(ResponseEntity<String> response) {
        // Si la respuesta es exitosa (2xx), devolver el JSON tal cual
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(response.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.getBody());
        }

        // Si es un error, verificar si es JSON o texto plano
        String responseBody = response.getBody();
        if (responseBody != null && !responseBody.trim().startsWith("{") && !responseBody.trim().startsWith("[")) {
            // Es texto plano, convertirlo a JSON con formato consistente
            return ResponseEntity.status(response.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", responseBody));
        }

        // Ya es JSON, devolverlo como está
        return ResponseEntity.status(response.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }
}

