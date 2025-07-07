package domain.colecciones.fuentes;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
public class FuenteId implements Serializable {
    private String id_interno;
    @Getter @Setter
    private String id_externo;

    public FuenteId() {}

    public FuenteId(String id_interno, String id_externo) {
        this.id_interno = id_interno;
        this.id_externo = id_externo;
    }

    // equals y hashCode (¡obligatorio!)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FuenteId that)) return false;
        return Objects.equals(id_interno, that.id_interno) &&
                Objects.equals(id_externo, that.id_externo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_interno, id_externo);
    }
}
