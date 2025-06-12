package domain.hechos.multimedias;

// VIDEO
public class Video extends Multimedia {
    private Integer resolucion;
    private Integer duracion;

    public Video(String formato, Integer tamanio, Integer resolucion, Integer duracion) {
        super(formato, tamanio);
        this.resolucion = resolucion;
        this.duracion = duracion;
    }

    @Override
    public void reproducir() {
        // TODO
    }
}