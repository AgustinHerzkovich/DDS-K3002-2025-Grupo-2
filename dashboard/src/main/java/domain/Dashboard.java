package domain;

import domain.connectionManager.ConnectionManager;
import domain.menu.MenuPrincipal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Dashboard {
     public static void main(String[] args) {
         SpringApplication.run(Dashboard.class, args);
        ConnectionManager.getInstance().registrarLocalHost(8084, "Agregador");
        ConnectionManager.getInstance().registrarLocalHost(8082, "Dinamica");
         new MenuPrincipal().abrirMenu();
         System.exit(0);
    }
}
