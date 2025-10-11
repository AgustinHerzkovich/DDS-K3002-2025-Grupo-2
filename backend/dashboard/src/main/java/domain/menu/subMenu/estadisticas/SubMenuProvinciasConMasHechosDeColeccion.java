package domain.menu.subMenu.estadisticas;

import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;
import domain.dtos.estadisticasDTOs.ProvinciaConMasHechosDTO;
import domain.dtos.estadisticasDTOs.ProvinciaConMasHechosDeColeccionDTO;

import java.util.List;
import java.util.Scanner;

public class SubMenuProvinciasConMasHechosDeColeccion {
    public static void abrirMenu(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el id de la coleccion: ");
        String coleccion = scanner.nextLine();

        System.out.print("Ingrese la cantidad límite de resultados: ");
        int limite = scanner.nextInt();
        scanner.nextLine();

        List<ProvinciaConMasHechosDeColeccionDTO> res = ApiClient.getProvinciasConMasHechosDeColeccion( coleccion, 0,limite, ConnectionManager.getInstance().getServidorLocal("Estadisticas"));
        res.forEach(System.out::println);

    }

}
