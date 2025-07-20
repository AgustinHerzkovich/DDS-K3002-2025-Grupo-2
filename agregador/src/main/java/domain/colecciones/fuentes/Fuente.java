package domain.colecciones.fuentes;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Fuente{
    @EmbeddedId
    private FuenteId id;
    private LocalDateTime ultimaPeticion;

    public Fuente(FuenteId id) {
        this.id = id;
        this.ultimaPeticion = null; // Arranca en null para que si es la primera petición, traer todos los hechos
    }
}