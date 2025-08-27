package domain.subMenu.apiAdministrativa;

import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;

public class SubMenuEnviarPostFuenteDinamica {
    public static void abrirMenu(){
        System.out.println("ID de fuente creada: " + ApiClient.postFuenteDinamica(ConnectionManager.getInstance().getServidorLocal("Dinamica")));
    }
}
