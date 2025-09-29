package domain.menu.subMenu.estadisticas;

import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;
import domain.dtos.estadisticasDTOs.CantidadSolicitudesSpamDTO;

import java.util.Scanner;

public class SubMenuObtenerSolicitudesSpam {
    public static void abrirMenu(){
        CantidadSolicitudesSpamDTO cantidadSolicitudesSpamDTO = ApiClient.getSolicitudesDeEliminacionSpam(ConnectionManager.getInstance().getServidorLocal("Estadisticas"));
        System.out.println();
        System.out.println(cantidadSolicitudesSpamDTO);
        System.out.println("Presione una tecla para volver");
        new Scanner(System.in).nextLine();
    }
}
