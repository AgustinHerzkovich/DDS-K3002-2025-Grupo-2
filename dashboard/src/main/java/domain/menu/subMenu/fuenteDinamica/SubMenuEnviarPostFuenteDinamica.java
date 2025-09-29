package domain.menu.subMenu.fuenteDinamica;

import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;

public class SubMenuEnviarPostFuenteDinamica {
    public static void abrirMenu(){
        System.out.println("Esta funcion fue deprecada. Volviendo al menu previo");
        //System.out.println("ID de fuente creada: " + ApiClient.postFuenteDinamica(ConnectionManager.getInstance().getServidorLocal("Dinamica")));
    }
}
