package domain.menu.subMenu.estadisticas;

import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;
import domain.dtos.estadisticasDTOs.ProvinciaConMasHechosDTO;

import java.util.List;
import java.util.Scanner;

public class SubMenuProvinciasConMasHechosDeCategoria {
    public static void abrirMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el nombre de la categoría: ");
        String categoria = scanner.nextLine();

        System.out.print("Ingrese la cantidad límite de resultados: ");
        int limite = scanner.nextInt();
        scanner.nextLine();

        List<ProvinciaConMasHechosDTO> res = ApiClient.getProvinciasConMasHechosDeCategoria( categoria, 0,limite, ConnectionManager.getInstance().getServidorLocal("Estadisticas"));
        res.forEach(System.out::println);

    }

}
