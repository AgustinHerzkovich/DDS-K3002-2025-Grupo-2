package domain.colecciones.fuentes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
public class HechoXFuenteId implements Serializable {

    @Column(name = "hecho_id")
    private Long hechoId;

    @Column(name = "fuente_id")
    private FuenteId fuenteId;

    public HechoXFuenteId(Long hechoId, FuenteId fuenteId) {
        this.hechoId = hechoId;
        this.fuenteId = fuenteId;
    }
}