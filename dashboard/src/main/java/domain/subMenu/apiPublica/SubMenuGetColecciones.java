package domain.subMenu.apiPublica;

import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;

public class SubMenuGetColecciones {
    public static void abrirMenu(){
        System.out.println("Colecciones: ");
        ApiClient.getColecciones(ConnectionManager.getInstance().getServidorLocal("Publica")).forEach(coleccionDTO -> System.out.println(coleccionDTO.toString()));
    }
}
