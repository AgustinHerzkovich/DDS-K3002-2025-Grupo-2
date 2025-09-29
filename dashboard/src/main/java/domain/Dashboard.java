package domain;

import domain.connectionManager.ConnectionManager;
import domain.menu.MenuPrincipal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Dashboard {
    public static void main(String[] args) {
        //SpringApplication.run(Dashboard.class, args);

        ConnectionManager.getInstance().registrarLocalHost(8085, "Publica");
        ConnectionManager.getInstance().registrarLocalHost(8086, "Admin");
        ConnectionManager.getInstance().registrarLocalHost(8082, "Dinamica");
        ConnectionManager.getInstance().registrarLocalHost(8087, "Estadisticas");
        System.out.println(ConnectionManager.getInstance().getServidorLocal("Dinamica"));
        System.out.println(ConnectionManager.getInstance().getServidorLocal("Admin"));
        System.out.println(ConnectionManager.getInstance().getServidorLocal("Publica"));
        System.out.println(ConnectionManager.getInstance().getServidorLocal("Estadisticas"));

        System.out.println(
                """
                        
                         /$$$$$$$                      /$$       /$$                                           /$$
                        | $$__  $$                    | $$      | $$                                          | $$
                        | $$  \\ $$  /$$$$$$   /$$$$$$$| $$$$$$$ | $$$$$$$   /$$$$$$   /$$$$$$   /$$$$$$   /$$$$$$$
                        | $$  | $$ |____  $$ /$$_____/| $$__  $$| $$__  $$ /$$__  $$ |____  $$ /$$__  $$ /$$__  $$
                        | $$  | $$  /$$$$$$$|  $$$$$$ | $$  \\ $$| $$  \\ $$| $$  \\ $$  /$$$$$$$| $$  \\__/| $$  | $$
                        | $$  | $$ /$$__  $$ \\____  $$| $$  | $$| $$  | $$| $$  | $$ /$$__  $$| $$      | $$  | $$
                        | $$$$$$$/|  $$$$$$$ /$$$$$$$/| $$  | $$| $$$$$$$/|  $$$$$$/|  $$$$$$$| $$      |  $$$$$$$
                        |_______/  \\_S______/|_______/ |__/  |__/|_______/  \\______/  \\_______/|__/       \\_______/
                        """);



        new MenuPrincipal().abrirMenu();
        System.exit(0);
    }
}
