package aplicacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Permitimos el acceso público a la raíz ('/'), y a todo lo que esté
                        // dentro de las carpetas /css/ y /images/ de nuestra carpeta 'static'.
                        .requestMatchers(
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/css/**"),
                                new AntPathRequestMatcher("/images/**")
                        ).permitAll()
                        // Cualquier otra petición (como /protegido) requerirá iniciar sesión.
                        .anyRequest().authenticated()
                )
                .oauth2Login(customizer -> customizer
                        .defaultSuccessUrl("/protegido", true) // Redirige aquí tras un login exitoso
                );

        return http.build();
    }
}