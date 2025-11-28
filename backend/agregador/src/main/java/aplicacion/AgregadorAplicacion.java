package aplicacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class AgregadorAplicacion {

  public static void main(String[] args) {
    SpringApplication.run(AgregadorAplicacion.class, args);
    System.out.println("Agregador iniciado");
  }
}