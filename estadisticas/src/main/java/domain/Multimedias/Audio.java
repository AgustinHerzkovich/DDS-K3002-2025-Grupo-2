package domain.Multimedias;


//AUDIO
public class Audio extends Multimedia {
    private Integer duracion;

    public Audio(String formato, Integer duracion) {
        this.formato = formato;
        this.duracion = duracion;
    }

    @Override
    public void reproducir() {
        // TODO
    }
}