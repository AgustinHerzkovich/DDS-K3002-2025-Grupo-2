package aplicacion.domain.hechos;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

//CATEGORIA
@Embeddable
@NoArgsConstructor
public class Categoria {
    @Getter
    private String nombre;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public Boolean esIdenticaA(String categoria_nombre) {
        return this.nombre.equals(categoria_nombre);
    }
}

