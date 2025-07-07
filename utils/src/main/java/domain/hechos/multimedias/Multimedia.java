package domain.hechos.multimedias;

import jakarta.persistence.*;

// MULTIMEDIA
@Entity
public abstract class Multimedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String formato;
    private Integer tamanio;

    public Multimedia(String formato, Integer tamanio){
        this.formato = formato;
        this.tamanio = tamanio;
    }

    public Multimedia() {

    }

    public abstract void reproducir();
}