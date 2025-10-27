package aplicacion.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Value("${interfaz.agregador.port}")
    private Integer interfazAgregadorPort;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Permitir peticiones desde el frontend en el puerto 8094
        config.setAllowedOrigins(Arrays.asList("http://localhost:" + interfazAgregadorPort));

        // Permitir todos los métodos HTTP (GET, POST, PUT, DELETE, etc.)
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Permitir todos los headers
        config.setAllowedHeaders(Arrays.asList("*"));

        // Permitir credenciales (cookies, authorization headers, etc.)
        config.setAllowCredentials(true);

        // Exponer todos los headers en la respuesta
        config.setExposedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}

