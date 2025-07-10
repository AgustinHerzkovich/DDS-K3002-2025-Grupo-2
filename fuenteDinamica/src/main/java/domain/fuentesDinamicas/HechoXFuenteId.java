package domain.fuentesDinamicas;

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
    private String hechoId;

    @Column(name = "fuente_id")
    private Long fuenteId;

    public HechoXFuenteId(String hechoId, Long fuenteId) {
        this.hechoId = hechoId;
        this.fuenteId = fuenteId;
    }
}