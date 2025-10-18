package aplicacion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                        // Spring Security 6+ usa RequestMatcher. La ruta debe ser el endpoint POST.
                        .ignoringRequestMatchers("/subir-hechos-post"))
            .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/solicitudes-pendientes").hasRole("ADMIN")
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/subir-hechos").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
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