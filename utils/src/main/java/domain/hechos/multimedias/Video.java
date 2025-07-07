package domain.hechos.multimedias;

import jakarta.persistence.Entity;

// VIDEO
@Entity
public class Video extends Multimedia {
    private Integer resolucion;
    private Integer duracion;

    public Video(String formato, Integer tamanio, Integer resolucion, Integer duracion) {
        super(formato, tamanio);
        this.resolucion = resolucion;
        this.duracion = duracion;
    }

    public Video() {

    }

    @Override
    public void reproducir() {
        // TODO
    }
}