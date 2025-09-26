package aplicacion.domain.hechosYSolicitudes;

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
    private String hechoId;
    private String coleccionId;

    public HechoXColeccionId() {}

    public HechoXColeccionId(String hechoId, String coleccionId) {
        this.hechoId = hechoId;
        this.coleccionId = coleccionId;
    }
}