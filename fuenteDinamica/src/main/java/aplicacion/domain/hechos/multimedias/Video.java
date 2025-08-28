package aplicacion.domain.hechos.multimedias;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// VIDEO
@Entity
@DiscriminatorValue("video")
@NoArgsConstructor
@Getter
@Setter
public class Video extends Multimedia {
    private String resolucion;
    private Integer duracion;

    public Video(String formato, Integer tamanio, String resolucion, Integer duracion) {
        super(formato, tamanio);
        this.resolucion = resolucion;
        this.duracion = duracion;
    }

    @Override
    public void reproducir() {
        // Implementar
    }
}