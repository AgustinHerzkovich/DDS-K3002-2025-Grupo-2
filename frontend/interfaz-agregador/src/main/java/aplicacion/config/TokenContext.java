package aplicacion.config;

import aplicacion.controllers.HechoController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;

public class TokenContext {
    private static final ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();
    private static OAuth2AuthorizedClientService clientService;
    private static final Logger logger = LoggerFactory.getLogger(TokenContext.class);

    // Metodo para inyectar el servicio desde un componente Spring
    public static void setClientService(OAuth2AuthorizedClientService service) {
        clientService = service;
    }

    public static void setToken(String token) {
        tokenThreadLocal.set(token);
    }

    public static String getToken() {
        // Primero intenta obtener del ThreadLocal
        String token = tokenThreadLocal.get();
        if (token != null) {
            return token;
        }

        // Si no está en ThreadLocal, obtenerlo directamente de Spring Security
        return getTokenFromSecurity();
    }

    private static String getTokenFromSecurity() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
                if (clientService != null) {
                    OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                            oauthToken.getAuthorizedClientRegistrationId(),
                            oauthToken.getName()
                    );
                    if (client != null && client.getAccessToken() != null) {
                        return client.getAccessToken().getTokenValue();
                    }
                }
            }
        } catch (Exception e) {
            // Log error but don't fail
            logger.error("Error obteniendo token de seguridad: {}", e.getMessage());
        }
        return null;
    }

    public static void clear() {
        tokenThreadLocal.remove();
    }

    public static void addToken(Model model){
        String token = getToken();
        if(token != null){
            model.addAttribute("JWT_Token", token);
        }
    }
}