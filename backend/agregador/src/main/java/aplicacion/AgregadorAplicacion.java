package aplicacion;

import aplicacion.controllers.HechoController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableDiscoveryClient
@EnableAsync
public class AgregadorAplicacion {
  private static final Logger logger = LoggerFactory.getLogger(AgregadorAplicacion.class);

  public static void main(String[] args) {
    SpringApplication.run(AgregadorAplicacion.class, args);
    logger.info("Agregador iniciado");
  }
}