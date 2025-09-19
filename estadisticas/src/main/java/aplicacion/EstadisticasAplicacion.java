package aplicacion;

import aplicacion.utils.IdentificadorDeUbicacion;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class EstadisticasAplicacion {
  public static void main(String[] args) {
    SpringApplication.run(EstadisticasAplicacion.class, args);
    System.out.println(IdentificadorDeUbicacion.getInstance().identificar( -34.687719, -59.132967

    ));
  }
}