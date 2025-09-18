package aplicacion.domain.id;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FactHechoId implements Serializable {
    private Long ubicacionId;
    private Long tiempoId;
    private Long categoriaId;

    public FactHechoId() {}

    public FactHechoId(Long ubicacionId, Long tiempoId, Long categoriaId) {
        this.ubicacionId = ubicacionId;
        this.tiempoId = tiempoId;
        this.categoriaId = categoriaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FactHechoId)) return false;
        FactHechoId that = (FactHechoId) o;
        return Objects.equals(ubicacionId, that.ubicacionId) &&
                Objects.equals(tiempoId, that.tiempoId) &&
                Objects.equals(categoriaId, that.categoriaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ubicacionId, tiempoId, categoriaId);
    }
}