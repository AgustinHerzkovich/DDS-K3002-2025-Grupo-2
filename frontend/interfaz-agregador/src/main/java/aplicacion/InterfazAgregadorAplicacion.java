package aplicacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InterfazAgregadorAplicacion {

  public static void main(String[] args) {
    SpringApplication.run(InterfazAgregadorAplicacion.class, args);
  }
}