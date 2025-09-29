package domain.menu.subMenu.estadisticas;

import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;
import domain.dtos.estadisticasDTOs.HoraConMasHechosDeCategoriaDTO;
import domain.dtos.estadisticasDTOs.ProvinciaConMasHechosDTO;

import java.util.List;
import java.util.Scanner;

public class SubMenuHorasConMasHechosDeCategoria {
    public static void abrirMenu(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el nombre de la categoría: ");
        String categoria = scanner.nextLine();

        System.out.print("Ingrese la cantidad límite de resultados: ");
        int limite = scanner.nextInt();
        scanner.nextLine();

        List<HoraConMasHechosDeCategoriaDTO> res = ApiClient.getHoraConMasHechosDeCategoria( categoria, 0,limite, ConnectionManager.getInstance().getServidorLocal("Estadisticas"));
        System.out.println("\n\nCategoria: " + categoria);
        System.out.println("Horas con mas hechos:");
        res.forEach(System.out::println);
    }
}
