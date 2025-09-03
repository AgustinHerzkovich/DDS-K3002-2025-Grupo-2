package aplicacion.domain.colecciones.fuentes;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Setter
public class FuenteId implements Serializable {
    private TipoFuente tipo;
    private Long idExterno;

    @JsonCreator
    public FuenteId(@JsonProperty("tipo") TipoFuente tipo, @JsonProperty("idExterno") Long idExterno) {
        this.tipo = tipo;
        this.idExterno = idExterno;
    }
}