package domain.hechos.multimedias;

//AUDIO
public class Audio extends Multimedia {
    private Integer duracion;

    public Audio(String formato, Integer tamanio, Integer duracion) {
        super(formato, tamanio);
        this.duracion = duracion;
    }

    @Override
    public void reproducir() {
        // TODO
    }
}