package aplicacion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/subir-hechos").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/", true)  // Forzar redirección después del login
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