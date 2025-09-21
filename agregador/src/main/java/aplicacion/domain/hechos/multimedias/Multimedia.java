package aplicacion.domain.hechos.multimedias;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// MULTIMEDIA
@Entity
@NoArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Multimedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String formato;
    private Integer tamanio;
    @Column(length = 500)
    private String url;

    public Multimedia(String formato, Integer tamanio, String url){
        this.formato = formato;
        this.tamanio = tamanio;
        this.url = url;
    }

    public abstract void reproducir();
}