package aplicacion.config; // Asegúrate de que este paquete sea escaneado

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration // CRUCIAL: Le dice a Spring que busque métodos @Bean aquí.
public class AppConfig {


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}