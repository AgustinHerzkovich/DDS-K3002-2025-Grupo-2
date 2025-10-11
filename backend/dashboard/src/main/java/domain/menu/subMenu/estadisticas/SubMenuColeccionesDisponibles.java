package domain.menu.subMenu.estadisticas;

import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;
import domain.dtos.estadisticasDTOs.ColeccionDisponibleDTO;

import java.util.List;
import java.util.Scanner;

public class SubMenuColeccionesDisponibles {
    public static void abrirMenu(){
        Scanner scanner = new Scanner(System.in);
        int page = 0;
        int limit = 5;
        String res = "";
        List<ColeccionDisponibleDTO> colecciones;
        do{
            System.out.println("\n");
            colecciones = ApiClient.getColeccionesDisponiblesDTO(ConnectionManager.getInstance().getServidorLocal("Estadisticas"), page, limit);
            colecciones.forEach(System.out::println);
            do{
                System.out.printf("\nPagina: %d\nS = salir, A = pagina anterior, D = pagina siguiente\n", page+1);
                res = scanner.nextLine().toLowerCase();
            }while (!res.equals("s") && !res.equals("a") && !res.equals("d"));
            if(res.equals("a") && page > 0){
                page--;
            }
            if(res.equals("d")){
                page++;
            }
        }while (!res.equals("s"));
    }
}
