package domain.Multimedias;

// IMAGEN
public class Imagen extends Multimedia {
    private String resolucion;

    public Imagen(String formato, String resolucion) {
        this.formato = formato;
        this.resolucion = resolucion;
    }

    @Override
    public void reproducir() {
        // TODO
    }
}
