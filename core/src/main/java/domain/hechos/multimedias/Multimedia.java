package domain.hechos.multimedias;

// MULTIMEDIA
public abstract class Multimedia {
    private String formato;
    private Integer tamanio;

    public Multimedia(String formato, Integer tamanio){
        this.formato = formato;
        this.tamanio = tamanio;
    }

    public abstract void reproducir();
}