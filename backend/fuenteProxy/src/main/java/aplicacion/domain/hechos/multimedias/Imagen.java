package aplicacion.domain.hechos.multimedias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// IMAGEN
@NoArgsConstructor
@Getter
@Setter
public class Imagen extends Multimedia {
    private String resolucion;

    public Imagen(String formato, Integer tamanio, String resolucion) {
        super(formato, tamanio);
        this.resolucion = resolucion;
    }

    @Override
    public void reproducir() {
        // Implementar
    }
}
