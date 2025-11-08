package aplicacion.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean("eurekaRestTemplate")
    @LoadBalanced
    public RestTemplate eurekaRestTemplate() {// ahora si quiero usar un servicio uso un eurekaRestTemplate
                                                // paso de hacer http://ip:puerto/endpoint
                                                // a hacer http://nombreDelComponente/endpoint

        return new RestTemplate();
    }

    @Bean("plainRestTemplate")
    public RestTemplate plainRestTemplate() {// si quiero consumir una api comun uso plainRestTemplate
        return new RestTemplate();
    }
}
