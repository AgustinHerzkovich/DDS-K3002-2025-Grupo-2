package domain.menu.subMenu.estadisticas;

import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;
import domain.dtos.estadisticasDTOs.CategoriaConMasHechosDTO;

import java.util.List;
import java.util.Scanner;

public class SubMenuCategoriasConMasHechos {
    public static void abrirMenu(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese la cantidad límite de resultados: ");
        int limite = scanner.nextInt();
        scanner.nextLine();


        List<CategoriaConMasHechosDTO> res = ApiClient.getCategoriasConMasHechos(0,limite, ConnectionManager.getInstance().getServidorLocal("Estadisticas"));
        System.out.println("Categorias con mas hechos:");
        res.forEach(System.out::println);

    }
}
