package domain.hechos.multimedias;

import jakarta.persistence.Entity;

// IMAGEN
@Entity
public class Imagen extends Multimedia {
    private String resolucion;

    public Imagen(String formato, Integer tamanio, String resolucion) {
        super(formato, tamanio);
        this.resolucion = resolucion;
    }

    public Imagen() {

    }

    @Override
    public void reproducir() {
        // TODO
    }
}
