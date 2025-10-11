package aplicacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FuenteProxyAplicacion {

  public static void main(String[] args) {
    SpringApplication.run(FuenteProxyAplicacion.class, args);
  }

}