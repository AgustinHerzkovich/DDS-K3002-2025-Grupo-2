package aplicacion.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements Filter {

    @Value("${api.rate.maxPerMinute}")
    Integer maximoPorMinuto;
    // Cache en memoria para guardar el "balde" de tokens asociado a cada IP
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Override
    // Este metodo se ejecuta en cada petición entrante
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 1. Identificamos al cliente por su dirección IP
        String ip = httpRequest.getRemoteAddr();

        // 2. Obtenemos el bucket de esa IP (o creamos uno nuevo si es la primera vez)
        Bucket bucket = cache.computeIfAbsent(ip, k -> createNewBucket());

        // 3. Intentamos consumir 1 token del balde
        if (bucket.tryConsume(1)) {
            // ÉXITO: Hay tokens disponibles, la petición pasa al Controller
            chain.doFilter(request, response);
        } else {
            // FALLO: Se acabaron los tokens (Rate Limit excedido)
            httpResponse.setStatus(429); // 429 Too Many Requests
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");

            // Devolvemos un mensaje JSON explicando el bloqueo
            String jsonError = "{\"error\": \"Has excedido el límite de solicitudes permitido. Por favor intenta nuevamente en unos segundos.\"}";
            httpResponse.getWriter().write(jsonError);
        }
    }

    /**
     * Configuración de la política de límites.
     * Aquí definimos cuántas peticiones se permiten por minuto.
     */
    private Bucket createNewBucket() {
        // Límite: maximoPorMinuto peticiones (capacidad) recargables cada 1 minuto.
        Bandwidth limit = Bandwidth.classic(maximoPorMinuto, Refill.greedy(maximoPorMinuto, Duration.ofMinutes(1)));

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
