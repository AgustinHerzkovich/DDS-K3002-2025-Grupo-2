package aplicacion;

import aplicacion.utils.IdentificadorDeUbicacion;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class EstadisticasAplicacion {
  public static void main(String[] args) {
    System.out.println("Estadisticas - Iniciando aplicación...");
    SpringApplication.run(EstadisticasAplicacion.class, args);
  }
}