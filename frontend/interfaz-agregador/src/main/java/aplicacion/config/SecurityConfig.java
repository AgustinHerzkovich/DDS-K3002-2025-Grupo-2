package aplicacion.config;

import aplicacion.services.CustomOidcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomOidcUserService customOidcUserService;
    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(CustomOidcUserService customOidcUserService) {
        this.customOidcUserService = customOidcUserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, KeycloakTokenRefreshFilter keycloakTokenRefreshFilter) throws Exception {
        http.csrf(csrf -> csrf
                        // Spring Security 6+ usa RequestMatcher. La ruta debe ser el endpoint POST.
                        .ignoringRequestMatchers("/gestionar-solicitud/{id}","/editarIdentidad"))
                .addFilterBefore(keycloakTokenRefreshFilter, OAuth2AuthorizationRequestRedirectFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        // Declaramos explícitamente todas las rutas públicas
                        .requestMatchers(
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/home"),
                                new AntPathRequestMatcher("/mapa"),
                                new AntPathRequestMatcher("/about"),
                                new AntPathRequestMatcher("/hechos/**"),
                                new AntPathRequestMatcher("/colecciones"),
                                new AntPathRequestMatcher("/colecciones/**"),
                                new AntPathRequestMatcher("/css/**"),
                                new AntPathRequestMatcher("/js/**"),
                                new AntPathRequestMatcher("/images/**"),
                                new AntPathRequestMatcher("/fragments/**"),
                                new AntPathRequestMatcher("/stats/**"),
                                new AntPathRequestMatcher("/favicon.ico"),
                                // Endpoints de Actuator para monitoreo (Prometheus, health checks, etc.)
                                new AntPathRequestMatcher("/actuator/**")
                        ).permitAll()
                        // Cualquier otra petición requerirá autenticación
                        .anyRequest().authenticated()
                ).sessionManagement(
                        session -> session.invalidSessionStrategy((request, response) -> {
                            if (request.getUserPrincipal() != null) { // Solo te manda a login si expiro la sesion
                                response.sendRedirect("/login?session=invalid");
                            }
                        })
                )
                .oauth2Login(oauth2 -> oauth2.loginPage("/oauth2/authorization/keycloak")
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(customOidcUserService)
                        )
                        .successHandler(successHandler)
                )
                // Configuración de Logout estándar que se integra con Keycloak
                .logout(logout -> logout
                        .logoutSuccessUrl("/")         // Redireccionar después del logout
                        .logoutUrl("/logout")          // URL para procesar el logout
                        .invalidateHttpSession(true)   // Invalidar sesión
                        .clearAuthentication(true)     // Limpiar autenticación
                        .deleteCookies("JSESSIONID")   // Eliminar cookies
                );

        return http.build();
    }
    @Bean
    public KeycloakTokenRefreshFilter keycloakTokenRefreshFilter(
            OAuth2AuthorizedClientManager clientManager, OAuth2AuthorizedClientRepository authorizedClientRepository) {
        return new KeycloakTokenRefreshFilter(clientManager, authorizedClientRepository);
    }

}