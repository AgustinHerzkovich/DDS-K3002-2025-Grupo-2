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
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/save-redirect-url"))
                .authorizeHttpRequests(authorize -> authorize
                        // permite acceso a recursos estáticos (CSS, JS, imágenes, etc.)
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/fragments/**").permitAll()
                        // protege el perfil
                        .requestMatchers("/profile").authenticated()
                        // el resto es público
                        .anyRequest().permitAll()
                )
                .oauth2Login(customizer -> customizer
                        .successHandler(successHandler));

        return http.build();
    }
}