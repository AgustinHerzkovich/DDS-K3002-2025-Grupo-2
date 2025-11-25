package domain.menu.subMenu.estadisticas;

import domain.apiClient.ApiClient;
import domain.connectionManager.ConnectionManager;

import java.util.Scanner;

public class SubMenuActualizarEstadisticas {
    public static void abrirMenu(){
        Scanner scanner = new Scanner(System.in);
        String rta;
        System.out.println("Esta seguro que desea actualizar las estadisticas?");
        System.out.println("Esto puede tardar un rato");
        System.out.println("(S/N)");
        do{rta = scanner.nextLine().toLowerCase();}
        while (!rta.equals("s") && !rta.equals("n"));
        if(rta.equals("n"))
            return;
        System.out.println();

        final Thread animacion = getAnimacionThread();

        animacion.start();
        ApiClient.postActualizarEstadisticas(ConnectionManager.getInstance().getServidorLocal("Estadisticas"));
        animacion.interrupt();
        try {
            animacion.join(); // Espera a que el hilo termine
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Estadisticas actualizadas");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @SuppressWarnings("BusyWait")
    private static Thread getAnimacionThread() {
        return new Thread(() -> {
            String[] puntos = {".  ", ".. ", "...", "   "};
            int i = 0;
            while (!Thread.currentThread().isInterrupted()) {
                System.out.print("\rActualizando estadísticas" + puntos[i % puntos.length]);
                i++;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }
}
