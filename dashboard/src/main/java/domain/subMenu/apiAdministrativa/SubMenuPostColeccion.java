package domain.subMenu.apiAdministrativa;

import domain.dashboardDTOs.ColeccionBuilder;
import domain.dashboardDTOs.ColeccionDTO;
import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;

import java.util.Scanner;

public class SubMenuPostColeccion {
    public static void abrirMenu(){
        System.out.println("Se enviará un post de coleccion");
        Scanner scanner = new Scanner(System.in);
        ColeccionBuilder builder = new ColeccionBuilder();

        System.out.println("=== Crear nueva Colección ===");

        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        builder.setTitulo(titulo);

        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();
        builder.setDescripcion(descripcion);

        System.out.print("ID fuente ESTATICA (int): \n -1 para cancelar");
        int idEstatica = Integer.parseInt(scanner.nextLine());
        if(idEstatica!=-1)
            builder.setEstatica(idEstatica);

        System.out.print("ID fuente DINAMICA (int): \n -1 para cancelar");
        int idDinamica = Integer.parseInt(scanner.nextLine());
        if (idDinamica!= -1)
            builder.setDinamica(idDinamica);

        System.out.print("Algoritmo de consenso (default: absoluto): ");
        String algoritmo = scanner.nextLine();
        if(algoritmo == null || algoritmo.isEmpty())
            builder.setAlgoritmo("absoluto");
        else
            builder.setAlgoritmo(algoritmo);

        ColeccionDTO coleccion = builder.buildColeccion();

        try {
            ApiClient.postColeccion(coleccion, ConnectionManager.getInstance().getServidorLocal("Admin"));
            System.out.println("\nColección creada:");
            System.out.println("Título: " + coleccion.getTitulo());
            System.out.println("Descripción: " + coleccion.getDescripcion());
            System.out.println("Fuentes: " );
            coleccion.getFuentes().forEach(f -> System.out.print(" - " +f.getId().getIdExterno() + " " + f.getId().getTipo() + "\n"));
            System.out.println("Algoritmo: " + coleccion.getAlgoritmoConsenso().getTipo());
            System.out.println("Criterios: " + coleccion.getCriteriosDePertenencia().size());
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

}
