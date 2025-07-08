package domain.colecciones.fuentes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class HechoXFuenteId implements Serializable {

    @Column(name = "hecho_id")
    private Long hechoId;

    @Column(name = "fuente_id")
    private FuenteId fuenteId;

    // Constructor vacío requerido por JPA
    public HechoXFuenteId() {}

    public HechoXFuenteId(Long hechoId, FuenteId fuenteId) {
        this.hechoId = hechoId;
        this.fuenteId = fuenteId;
    }

    // Getters y Setters

    // equals y hashCode (¡muy importante para claves compuestas!)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HechoXFuenteId that)) return false;
        return Objects.equals(hechoId, that.hechoId) &&
                Objects.equals(fuenteId, that.fuenteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hechoId, fuenteId);
    }
}