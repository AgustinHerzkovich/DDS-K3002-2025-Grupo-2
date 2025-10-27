package aplicacion.config;

import aplicacion.dtos.input.ContribuyenteInputDto;
import aplicacion.dtos.input.IdentidadContribuyenteInputDto;
import aplicacion.dtos.output.ContribuyenteOutputDto; // Clase que contiene el ID de la base de datos
import aplicacion.services.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired; // Necesario si no usas @Component
import org.springframework.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;


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

        //  Si la autenticación fue exitosa y es de tipo OAuth2/OIDC
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

        // Lógica de Redirección
        String targetUrl = (String) request.getSession().getAttribute("REDIRECT_URL_AFTER_LOGIN");

        if (targetUrl == null || targetUrl.isEmpty()) {
            targetUrl = "/";
        }

        request.getSession().removeAttribute("REDIRECT_URL_AFTER_LOGIN");
        response.sendRedirect(targetUrl);
    }
}