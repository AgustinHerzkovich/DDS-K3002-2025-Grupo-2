package aplicacion.domain.id;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FactColeccionId implements Serializable {
    private Long coleccionId;
    private Long ubicacionId;
    private Long tiempoId;
    private Long categoriaId;

    // Constructores, equals y hashCode son OBLIGATORIOS
    public FactColeccionId() {}

    public FactColeccionId(Long coleccionId, Long ubicacionId, Long tiempoId, Long categoriaId) {
        this.coleccionId = coleccionId;
        this.ubicacionId = ubicacionId;
        this.tiempoId = tiempoId;
        this.categoriaId = categoriaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FactColeccionId)) return false;
        FactColeccionId that = (FactColeccionId) o;
        return Objects.equals(coleccionId, that.coleccionId) &&
                Objects.equals(ubicacionId, that.ubicacionId) &&
                Objects.equals(tiempoId, that.tiempoId) &&
                Objects.equals(categoriaId, that.categoriaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coleccionId, ubicacionId, tiempoId, categoriaId);
    }
}