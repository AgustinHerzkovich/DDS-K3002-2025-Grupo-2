package domain;

import domain.connectionManager.ConnectionManager;
import domain.menu.MenuPrincipal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Dashboard {
    public static void main(String[] args) {
        //SpringApplication.run(Dashboard.class, args);


        System.out.println(
                "⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                "⣶⣬⣛⠻⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                "⣟⡻⢿⣿⣶⣬⣙⠻⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                "⣿⣿⣷⣮⣝⡻⠿⣿⣶⣬⣙⡻⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠛⣿⣿⣿\n" +
                "⣿⣭⣛⠻⢿⣿⣷⣦⣝⡛⠿⣿⣷⣦⣍⡛⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⠇⣿⣿⣿\n" +
                "⣿⠿⣿⣿⣷⣮⣝⡛⠿⣿⣷⣦⣍⣛⠿⣿⣷⣦⣍⡛⠿⢿⣿⣿⣿⣿⠿⠟⡛⠻⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                "⣿⣿⣦⣍⣛⠿⣿⣿⣷⣦⣍⣛⠿⣿⣿⣶⣭⣛⠿⢿⣿⣶⣬⡙⠋⠐⠾⢿⣿⣿⣷⣦⣭⣙⠻⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                "⣬⣙⡻⢿⣿⣿⣶⣬⣙⡻⢿⣿⣿⣶⣭⣙⠻⢿⣿⣶⣬⣙⠛⢠⣾⣿⣿⣶⣬⣍⡛⠿⢿⣿⣿⣶⣦⣍⣛⠿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                "⠿⣿⣿⣷⣦⣍⡛⠿⣿⣿⣷⣮⣝⡛⠿⣿⣿⣶⣮⡝⠻⠿⡇⣿⣿⡿⠟⠛⠿⣿⣿⣿⣶⣬⣍⡛⠻⢿⣿⣿⣶⣬⣉⡛⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                "⣿⣶⣭⣙⠻⢿⣿⣷⣦⣍⣛⠿⣿⣿⣷⣦⣍⣛⠿⢡⣶⡀⡆⣿⣿⡇⣾⣿⣷⣶⣬⣙⠻⠿⣿⣿⣿⣶⣦⣍⡛⠻⢿⣿⣷⣬⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                "⣬⣙⠻⢿⣿⣶⣬⣝⡻⠿⣿⣿⣶⣭⣛⠻⢿⣿⡇⢸⣿⡇⠃⣿⣿⡇⣿⣟⠉⠙⣿⣿⣿⣷⣦⠍⠙⠻⠿⣿⣿⣿⣦⠹⣿⣿⣧⠸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                "⣿⣿⣿⣶⣬⣙⠻⢿⣿⣷⣦⣍⡛⠿⣿⣿⣶⣬⣉⠸⣿⣧⠀⣿⣿⡇⣿⣿⣶⣾⣿⣿⣿⣿⣧⡐⠂⣭⡐⠦⡙⢿⣿⣧⢸⣿⣿⡇⣿⡿⢛⣉⠹⣿⣿⣿⣿⣿⣿⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣶⣬⣙⠻⢿⣿⣷⣦⣍⣛⠿⣿⣧⡙⢿⡇⣿⣿⡇⠘⣿⣿⣿⣿⢛⠻⣿⣿⣿⠀⣿⣿⣷⡀⢸⣿⣿⢸⣿⣿⡇⠉⣴⣿⣿⠀⣿⣿⣿⣿⣿⡿⣡\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣬⣝⡻⠿⣿⣿⣶⣬⣙⠀⠀⣿⣿⠁⣶⣿⣿⣿⣧⣬⣤⣿⠟⠛⡇⢹⣿⣿⣷⣀⣉⣩⣤⣭⣍⣁⣼⣿⣿⣿⠀⣿⣿⣿⣿⣿⢡⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣮⣍⡛⠿⣿⣿⡆⢿⣿⡄⢿⣿⡋⠉⢹⣿⣿⣿⣤⣤⢀⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⠹⣿⣿⣿⢇⣾⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣦⣍⡛⠘⣿⣿⣄⠙⠿⣶⣾⣿⣟⢩⢙⡇⣾⣿⣿⣿⡁⠀⢈⣿⣿⣿⣿⣿⣿⡏⠀⠈⢹⡆⢻⣿⣡⣾⣿⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⡌⢠⣿⡇⢶⣦⣍⡛⠿⣶⣿⡇⠛⢫⣯⡝⣷⣴⣾⣿⣿⣇⠀⣸⣿⣿⣶⡎⣴⡇⢸⣿⣿⣿⣿⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣇⠸⡿⢁⣈⡛⠿⣿⣷⣶⣬⣙⠀⠘⠻⣃⣾⣿⠹⣿⣿⣿⠁⣿⣿⡟⢻⣧⡙⢁⣿⣿⣿⣿⣿⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣶⣾⣿⣿⣷⣦⣍⣙⠻⢿⣿⣶⣤⣙⠻⠿⣶⣬⣭⣤⣶⣤⡁⠰⠟⣋⣴⣿⣿⣿⣿⣿⣿⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣬⣙⠻⢿⠟⣠⣶⡶⡄⣤⣤⡄⢰⣶⡌⢻⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡶⢰⣿⣿⠀⠰⠿⢛⣀⡘⠿⣿⠀⣿⣿⣿⣿⣿⣿⣿⣿\n" +
                "⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠌⣉⣡⣾⣿⣿⣿⣿⣿⣷⣶⣾⣿⣿⣿⣿⣿⣿⣿⣿");



        ConnectionManager.getInstance().registrarLocalHost(8085, "Publica");
        ConnectionManager.getInstance().registrarLocalHost(8086, "Admin");
        ConnectionManager.getInstance().registrarLocalHost(8082, "Dinamica");

        System.out.println(ConnectionManager.getInstance().getServidorLocal("Dinamica"));
        System.out.println(ConnectionManager.getInstance().getServidorLocal("Admin"));
        System.out.println(ConnectionManager.getInstance().getServidorLocal("Publica"));

        new MenuPrincipal().abrirMenu();
        System.exit(0);
    }
}
