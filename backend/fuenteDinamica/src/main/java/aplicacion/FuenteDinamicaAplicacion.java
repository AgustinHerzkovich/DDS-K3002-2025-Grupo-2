package aplicacion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FuenteDinamicaAplicacion {
  @Value("8094")
  private Integer interfazAgregadorPort;

  public static void main(String[] args) {
    SpringApplication.run(FuenteDinamicaAplicacion.class, args);
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://interfaz-agregador:"+interfazAgregadorPort).allowedMethods("*").allowedHeaders("*"); //establece que front puede hacer peticiones al back, donde puede definir los metodos y headers permitidos
      }
    };
  }
}