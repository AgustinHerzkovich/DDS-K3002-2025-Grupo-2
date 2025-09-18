package aplicacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "aplicacion.repositorios")
public class EstadisticasAplicacion {
  public static void main(String[] args) {

    SpringApplication.run(EstadisticasAplicacion.class, args);

  }
}