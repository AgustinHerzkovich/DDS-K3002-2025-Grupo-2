package domain.colecciones.fuentes;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
public class FuenteId implements Serializable {
    private String idInterno;
    private Long idExterno;


    public FuenteId(String idInterno, Long idExterno) {
        this.idInterno = idInterno;
        this.idExterno = idExterno;
    }
}