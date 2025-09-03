package aplicacion.domain.hechos.multimedias;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// IMAGEN
@Entity
@DiscriminatorValue("imagen")
@NoArgsConstructor
@Getter
@Setter
public class Imagen extends Multimedia {
    @Column(length = 20)
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
