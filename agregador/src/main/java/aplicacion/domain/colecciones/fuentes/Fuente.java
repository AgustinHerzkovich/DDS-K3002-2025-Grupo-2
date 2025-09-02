package aplicacion.domain.colecciones.fuentes;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Fuente{
    @EmbeddedId
    private FuenteId id;

    public Fuente(FuenteId id) {
        this.id = id;
    }
}