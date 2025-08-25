package domain.subMenu;

import domain.DTOs.HechoPostBuilder;
import domain.DTOs.PostContribuyenteDTO;
import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;

import java.util.Scanner;

public class SubMenuPostContribuyente {
    public static void abrirMenu(){
        System.out.println("Se enviara un post de contribuyente a fuenteDinamica.");
        System.out.println("Ingrese si el usuario es Aministrador (True o False)");
        Scanner scanner = new Scanner(System.in);
        PostContribuyenteDTO contribuyenteDTO = new PostContribuyenteDTO(Boolean.parseBoolean(scanner.nextLine()));
        Integer respuesta = ApiClient.postContribuyente(contribuyenteDTO, ConnectionManager.getInstance().getServidorLocal("Dinamica"));
        SubMenuPatchIdentidad.lastContribuyenteId = respuesta;
        System.out.println("Se recibio la respuesta: El contribuyente numero " + respuesta + " fue creado.");
    }
}
