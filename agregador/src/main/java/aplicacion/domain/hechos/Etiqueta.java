package aplicacion.domain.hechos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

// ETIQUETA
@NoArgsConstructor
@Entity
@Getter
public class Etiqueta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20)
    private String nombre;

    public Etiqueta(String nombre){
        this.nombre = nombre;
    }

    public boolean esIdenticaA(String etiquetaNombre) {
        return this.nombre.equals(etiquetaNombre);
    }
}