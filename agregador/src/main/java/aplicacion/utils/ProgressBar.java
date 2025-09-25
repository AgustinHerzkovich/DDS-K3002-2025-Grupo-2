package aplicacion.utils;



// Importante. Solo usar una a la vez
public class ProgressBar {
    private final int total;
    private final int largoBarra;
    private int progresoActual = 0;
    private final String title;

    public ProgressBar(int total) {
        this(total, 50, null);
    }

    public ProgressBar(int total, String title) {
        this(total, 50, title);
    }
    public ProgressBar(int total, int largoBarra) {
        this(total, largoBarra, null);
    }

    public ProgressBar(int total, int largoBarra, String title) {
        this.total = total;
        this.largoBarra = largoBarra;
        this.title = title;
    }

    public void avanzar() {
        avanzar(1);
    }

    public void avanzar(int cantidad) {
        progresoActual += cantidad;
        imprimir();
    }

    private void imprimir() {
        int porcentaje = (int) ((double) progresoActual / total * 100);
        int llenos = (porcentaje * largoBarra) / 100;
        int vacios = largoBarra - llenos;

        String barra = "[" + "#".repeat(llenos) + "-".repeat(vacios) + "] " +
                porcentaje + "% (" + progresoActual + "/" + total + (title != null? ") - " + title : ")");
        System.out.print("\r" + barra);
        if (progresoActual >= total) {
            System.out.print("\n");
        }
    }
}
