package aplicacion.domain.colecciones.fuentes;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class FuenteXColeccionId {
    private FuenteId fuenteId;
    private String coleccionId;


    public FuenteXColeccionId(FuenteId fuenteId, String coleccionId) {
        this.fuenteId = fuenteId;
        this.coleccionId = coleccionId;
    }
}
