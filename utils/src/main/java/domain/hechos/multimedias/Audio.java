package domain.hechos.multimedias;

import jakarta.persistence.Entity;

//AUDIO
@Entity
public class Audio extends Multimedia {
    private Integer duracion;

    public Audio(String formato, Integer tamanio, Integer duracion) {
        super(formato, tamanio);
        this.duracion = duracion;
    }

    public Audio() {

    }

    @Override
    public void reproducir() {
        // TODO
    }
}