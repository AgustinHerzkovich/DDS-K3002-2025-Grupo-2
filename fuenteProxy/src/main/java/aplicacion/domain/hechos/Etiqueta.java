package aplicacion.domain.hechos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

// ETIQUETA
@NoArgsConstructor
@Entity
public class Etiqueta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String nombre;

    public Etiqueta(String nombre){
        this.nombre = nombre;
    }

    public boolean esIdenticaA(String etiquetaNombre) {
        return this.nombre.equals(etiquetaNombre);
    }
}