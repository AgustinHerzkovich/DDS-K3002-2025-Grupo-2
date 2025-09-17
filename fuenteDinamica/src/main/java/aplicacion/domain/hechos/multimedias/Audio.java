package aplicacion.domain.hechos.multimedias;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//AUDIO
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Audio extends Multimedia {
    private Integer duracion;

    public Audio(String formato, Integer tamanio, Integer duracion) {
        super(formato, tamanio);
        this.duracion = duracion;
    }

    @Override
    public void reproducir() {
        // Implementar
    }
}