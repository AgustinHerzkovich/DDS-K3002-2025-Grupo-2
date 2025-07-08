package domain.colecciones.fuentes;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
public class FuenteId implements Serializable {
    private String idInterno;
    @Getter @Setter
    private String idExterno;

    public FuenteId() {}

    public FuenteId(String idInterno, String idExterno) {
        this.idInterno = idInterno;
        this.idExterno = idExterno;
    }

    // equals y hashCode (¡obligatorio!)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FuenteId that)) return false;
        return Objects.equals(idInterno, that.idInterno) &&
                Objects.equals(idExterno, that.idExterno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInterno, idExterno);
    }
}