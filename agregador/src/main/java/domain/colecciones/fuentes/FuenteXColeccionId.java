package domain.colecciones.fuentes;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class FuenteXColeccionId {
    private FuenteId fuenteId;
    private String coleccionId;


    public FuenteXColeccionId(FuenteId fuenteId, String coleccionId) {
        this.fuenteId = fuenteId;
        this.coleccionId = coleccionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FuenteXColeccionId)) return false;
        FuenteXColeccionId that = (FuenteXColeccionId) o;
        return Objects.equals(fuenteId, that.fuenteId) &&
                Objects.equals(coleccionId, that.coleccionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fuenteId, coleccionId);
    }
}
