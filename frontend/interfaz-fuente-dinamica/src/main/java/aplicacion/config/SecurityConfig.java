package aplicacion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import aplicacion.services.CustomOidcUserService;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
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
                        .ignoringRequestMatchers("/subir-hechos-post","/guardar-edicion/{id}","/editarIdentidad","/gestionar-solicitud/{id}"))
            .authorizeHttpRequests(authorize -> authorize
                        //.requestMatchers("/solicitudes-pendientes").hasRole("ADMIN")
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/subir-hechos").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(customOidcUserService)
                        )
                        .successHandler(successHandler)
                )
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

/*.csrf(csrf -> csrf
        // Spring Security 6+ usa RequestMatcher. La ruta debe ser el endpoint POST.
        .ignoringRequestMatchers("/subir-hechos-post","/guardar-edicion/{id}","/editarIdentidad"))*/