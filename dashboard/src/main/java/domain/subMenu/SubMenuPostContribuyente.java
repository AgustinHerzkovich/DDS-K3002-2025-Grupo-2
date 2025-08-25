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
        System.out.println("Default: false");
        Scanner scanner = new Scanner(System.in);
        Boolean admin;
        try{ admin = Boolean.parseBoolean(scanner.nextLine());} catch (Exception e){
            System.out.println("No se entendio el valor ingresado. Se establecio el valor por defecto (false)");
            admin = false;
        }
        PostContribuyenteDTO contribuyenteDTO = new PostContribuyenteDTO();
        Integer respuesta = ApiClient.postContribuyente(contribuyenteDTO, ConnectionManager.getInstance().getServidorLocal("Dinamica"));
        SubMenuPatchIdentidad.lastContribuyenteId = respuesta;
        System.out.println("Se recibio la respuesta: El contribuyente numero " + respuesta + " fue creado.");
    }
}
