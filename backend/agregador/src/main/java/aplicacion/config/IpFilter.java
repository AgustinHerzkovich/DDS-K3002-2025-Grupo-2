package aplicacion.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class IpFilter implements Filter {

    @Value("${security.active}")
    boolean seguridadActiva;
    // =========================================================================
    // 1. LISTA NEGRA (BLACKLIST)
    // Las IPs en esta lista serán rechazadas inmediatamente.
    // Se colocaron placeholders de ejemplo
    // =========================================================================
    private final List<String> blacklist = Arrays.asList(
            "192.168.1.66",
            "10.0.0.55",
            "1.2.3.4"
    );

    // "127.0.0.1",      // Localhost IPv4
    // "0:0:0:0:0:0:0:1" // Localhost IPv6
    // =========================================================================
    // 2. LISTA BLANCA (WHITELIST)
    // Por ahora se deja vacía para permitir el acceso público general.
    // =========================================================================
    private final List<String> whitelist = List.of();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if(!seguridadActiva){
            chain.doFilter(request, response);
            return;
        }

        // Obtenemos la IP de quien nos llama
        String clientIp = httpRequest.getRemoteAddr();

        // A. Verificación de LISTA BLANCA (Modo Estricto)
        // Si la lista blanca NO está vacía, verificamos que la IP esté ahí.
        if (!whitelist.isEmpty() && !whitelist.contains(clientIp)) {
            blockRequest(httpResponse, "Acceso restringido: Tu IP no está en la lista autorizada.");
            return; // Cortamos la ejecución
        }

        // B. Verificación de LISTA NEGRA
        // Si la IP está en la lista negra, la bloqueamos.
        if (blacklist.contains(clientIp)) {
            blockRequest(httpResponse, "Acceso denegado: Tu IP ha sido bloqueada por seguridad.");
            return; // Cortamos la ejecución
        }

        // Si pasa los controles, adelante.
        chain.doFilter(request, response);
    }

    private void blockRequest(HttpServletResponse response, String message) throws IOException {
        response.setStatus(403); // 403 Forbidden (Prohibido)
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
