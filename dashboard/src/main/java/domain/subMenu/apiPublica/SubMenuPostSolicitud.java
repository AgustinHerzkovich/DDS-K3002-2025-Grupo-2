package domain.subMenu.apiPublica;

import domain.dashboardDTOs.usuarios.ContribuyenteSolicitudDTO;
import domain.dashboardDTOs.solicitudes.SolicitudDTO;
import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;

import java.util.Scanner;

public class SubMenuPostSolicitud {
    public static void abrirMenu(){
        SolicitudDTO solicitudDTO;
        System.out.println("Aun no implementado");
        System.out.println("Ingrese el ID del hecho");
        Scanner scanner = new Scanner(System.in);
        String idHecho = scanner.nextLine();
        if(idHecho == null || idHecho.isBlank()){
            System.out.println("No se ingreso un ID: solicitud cancelada");
            return;
        }
        System.out.println("Ingrese el motivo por el que quiere eliminar el hecho");
        System.out.println("Default: Porque si");
        String motivo = scanner.nextLine();
        motivo = (motivo == null || motivo.isBlank()) ? "Porque si" : motivo;

        System.out.println("Ingrese el id del contribuyente que solicita la eliminacion");
        System.out.println("Default: 1");
        Integer idContribuyente;
        try {
            idContribuyente = Integer.valueOf(scanner.nextLine());
        }catch (Exception e){
            System.out.println("Ok, id puesto como 1");
            idContribuyente = 1;
        }

        ApiClient.postSolicitud(new SolicitudDTO(new ContribuyenteSolicitudDTO(idContribuyente), idHecho, motivo), ConnectionManager.getInstance().getServidorLocal("Publica"));

    }
}
