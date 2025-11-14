package aplicacion.config;

import aplicacion.services.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UsuarioService usuarioService;

    @Autowired
    public CustomAuthenticationSuccessHandler(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        if (authentication instanceof OAuth2AuthenticationToken) {
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            // Ejecutar la lógica de creación de contribuyente y guardar el ID
            usuarioService.registrarUsuarioSiNoExiste(oidcUser, request);
        }

        System.out.println("=== Authorities del usuario ===");
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            System.out.println(" -> " + authority.getAuthority());
        }
        System.out.println("==============================");

        String targetUrl = (String) request.getSession().getAttribute("REDIRECT_URL_AFTER_LOGIN");

        if (targetUrl == null || targetUrl.isEmpty()) {
            targetUrl = "/";
        }

        request.getSession().removeAttribute("REDIRECT_URL_AFTER_LOGIN");
        response.sendRedirect(targetUrl);
    }
}

