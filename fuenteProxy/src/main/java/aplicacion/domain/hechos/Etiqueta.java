package aplicacion.domain.hechos;

import lombok.Getter;
import lombok.NoArgsConstructor;

// ETIQUETA
@NoArgsConstructor
public class Etiqueta {
    @Getter
    private String nombre;

    public Etiqueta(String nombre){
        this.nombre = nombre;
    }

    public boolean esIdenticaA(String etiquetaNombre) {
        return this.nombre.equals(etiquetaNombre);
    }
}