package domain.hechos.multimedias;

// IMAGEN
public class Imagen extends Multimedia {
    private String resolucion;

    public Imagen(String formato, Integer tamanio, String resolucion) {
        super(formato, tamanio);
        this.resolucion = resolucion;
    }

    @Override
    public void reproducir() {
        // TODO
    }
}
