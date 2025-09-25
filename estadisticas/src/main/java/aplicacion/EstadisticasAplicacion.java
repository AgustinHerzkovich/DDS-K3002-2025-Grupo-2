package aplicacion;

import aplicacion.domain.facts.FactHecho;
import aplicacion.domain.id.FactHechoId;
import aplicacion.repositorios.olap.FactHechoRepository;
import aplicacion.utils.IdentificadorDeUbicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import java.util.Optional;


@SpringBootApplication
public class EstadisticasAplicacion {
  public static void main(String[] args) {
    System.out.println("Estadisticas - Iniciando aplicación...");
    SpringApplication.run(EstadisticasAplicacion.class, args);
  }
}


