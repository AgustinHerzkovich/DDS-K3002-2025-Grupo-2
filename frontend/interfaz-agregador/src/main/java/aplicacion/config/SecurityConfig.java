package aplicacion.config;

import aplicacion.services.CustomOidcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                        // Spring Security 6+ usa RequestMatcher. La ruta debe ser el endpoint POST.
                        .ignoringRequestMatchers("/gestionar-solicitud/{id}"))
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
                                new AntPathRequestMatcher("/favicon.ico")
                        ).permitAll()
                        // Cualquier otra petición requerirá autenticación
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
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

}