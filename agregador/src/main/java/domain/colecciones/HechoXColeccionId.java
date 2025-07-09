package domain.colecciones;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Embeddable
@EqualsAndHashCode
public class HechoXColeccionId implements Serializable {
    private Long hechoId;
    private String coleccionId;

    public HechoXColeccionId() {}

    public HechoXColeccionId(Long hechoId, String coleccionId) {
        this.hechoId = hechoId;
        this.coleccionId = coleccionId;
    }
}