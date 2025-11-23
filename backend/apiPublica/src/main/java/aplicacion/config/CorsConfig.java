package aplicacion.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig {

    @Value("${interfaz.agregador.port}")
    private Integer interfazAgregadorPort;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Esto permite peticiones desde Docker, Postman, Browser, etc.
        config.setAllowedOriginPatterns(Collections.singletonList("*"));

        // Permitir todos los métodos HTTP (GET, POST, PUT, DELETE, etc.)
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));

        // Permitir todos los headers
        config.setAllowedHeaders(Arrays.asList("*"));

        // Permitir credenciales (cookies, authorization headers, etc.)
        config.setAllowCredentials(true);

        // Exponer headers útiles
        config.setExposedHeaders(Arrays.asList("Authorization", "Link", "X-Total-Count"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}

