package aplicacion.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
public class KeycloakTokenRefreshFilter extends OncePerRequestFilter {

    private final OAuth2AuthorizedClientManager clientManager;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;

    public KeycloakTokenRefreshFilter(OAuth2AuthorizedClientManager clientManager, OAuth2AuthorizedClientRepository authorizedClientRepository) {
        this.clientManager = clientManager;
        this.authorizedClientRepository = authorizedClientRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String uri = req.getRequestURI();

        // Ignoro las URLs de login ya que voy a sobreescribir la cookie con eso
        if (uri.startsWith("/oauth2/authorization") || uri.startsWith("/login/oauth2")) {
            chain.doFilter(req, res);
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Si no hay autenticación o es anónima, seguir normalmente
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            chain.doFilter(req, res);
            return;
        }

        // Si hay autenticación, cargamos el OAuth2AuthorizedClient
        OAuth2AuthorizedClient client = authorizedClientRepository.loadAuthorizedClient("keycloak", auth, req);

        if (client != null) {
            // Token válido: intentamos refrescarlo y seguimos
            clientManager.authorize(
                    OAuth2AuthorizeRequest.withClientRegistrationId("keycloak")
                            .principal(auth)
                            .build()
            );
            chain.doFilter(req, res);
            return;
        }

        // Autenticación existe pero el client no es válido
        // logout y recargar la página
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        res.addCookie(cookie);
        res.sendRedirect(req.getRequestURI());
    }


    /* Metodo viejo que anda pero es confuso
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String uri = req.getRequestURI();
        // Ignoro las url de login, ya que si estoy ahi es porque voy a sobreescribir mi cookie. No me importa la sesion ni nada
        if (uri.startsWith("/oauth2/authorization") || uri.startsWith("/login/oauth2")) {
            chain.doFilter(req, res);
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean hasClient = false;

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            // Si esta autenticado
            OAuth2AuthorizedClient client = authorizedClientRepository.loadAuthorizedClient("keycloak", auth, req);

            if (client != null) {   // Si la autenticacion es valida
                clientManager.authorize(
                        OAuth2AuthorizeRequest.withClientRegistrationId("keycloak")
                                .principal(auth)
                                .build()
                );  // Intento refrescar el token
                hasClient = true;
            }
        }

        if (!hasClient && auth != null && auth.isAuthenticated()) {
            // Si la autenticacion existe pero es invalida
            // Esto ocurre si la sesión del servidor se perdió pero la cookie JSESSIONID aún existe


            // Con esta opcion me manda directo al login
            // res.sendRedirect("/oauth2/authorization/keycloak");

            // Con esta opcion nomas me hace logout, eliminando la cookie
            Cookie cookie = new Cookie("JSESSIONID", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            res.addCookie(cookie);
            res.sendRedirect(req.getRequestURI());
            return;
        }
        // Si no esta autenticado llega hasta aca y sigue de largo
        chain.doFilter(req, res);
    }*/
}