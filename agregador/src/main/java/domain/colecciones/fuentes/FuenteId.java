package domain.colecciones.fuentes;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    public FuenteId(@JsonProperty("idExterno") Long idExterno) {
        this.idInterno = UUID.randomUUID().toString();
        this.idExterno = idExterno;
    }
}