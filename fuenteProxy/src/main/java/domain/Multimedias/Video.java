package domain.Multimedias;

// VIDEO
public class Video extends Multimedia {
    private Integer resolucion;
    private Integer duracion;

    public Video(String formato, Integer resolucion, Integer duracion) {
        this.formato = formato;
        this.resolucion = resolucion;
        this.duracion = duracion;
    }

    @Override
    public void reproducir() {
        // TODO
    }
}