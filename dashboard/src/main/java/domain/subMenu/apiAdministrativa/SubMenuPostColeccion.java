package domain.subMenu.apiAdministrativa;

import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;
import domain.dashboardDTOs.colecciones.ColeccionBuilder;
import domain.dashboardDTOs.colecciones.ColeccionDTO;

import java.util.Scanner;

public class SubMenuPostColeccion {
    public static void abrirMenu() {
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

        System.out.print("ID fuente ESTATICA (int):");
        String idEstaticaInput = scanner.nextLine();
        if (!idEstaticaInput.isEmpty()) {
            try {
                int idEstatica = Integer.parseInt(idEstaticaInput);
                builder.setEstatica(idEstatica);
            } catch (Exception e) {
                System.out.println("No se recibio un entero. No se incluira una fuente estatica");
            }
        }

        System.out.print("ID fuente DINAMICA (int):");
        String idDinamicaInput = scanner.nextLine();
        if (!idDinamicaInput.isEmpty()) {
            try {
                int idDinamica = Integer.parseInt(idDinamicaInput);
                builder.setDinamica(idDinamica);
            } catch (Exception e) {
                System.out.println("No se recibio un entero. No se incluirá una fuente dinamica");
            }
        }

        System.out.print("Algoritmo de consenso (default: absoluto): ");
        String algoritmo = scanner.nextLine();
        if (algoritmo == null || algoritmo.isEmpty())
            builder.setAlgoritmo("absoluto");
        else
            builder.setAlgoritmo(algoritmo);

        ColeccionDTO coleccion = builder.buildColeccion();

        try {
            ApiClient.postColeccion(coleccion, ConnectionManager.getInstance().getServidorLocal("Admin"));
            System.out.println("\nColección creada:");
            System.out.println("Título: " + coleccion.getTitulo());
            System.out.println("Descripción: " + coleccion.getDescripcion());
            System.out.println("Fuentes: ");
            coleccion.getFuentes().forEach(f -> System.out.print(" - " + f.getId().getIdExterno() + " " + f.getId().getTipo() + "\n"));
            System.out.println("Algoritmo: " + coleccion.getAlgoritmoConsenso().getTipo());
            System.out.println("Criterios: " + coleccion.getCriteriosDePertenencia().size());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
