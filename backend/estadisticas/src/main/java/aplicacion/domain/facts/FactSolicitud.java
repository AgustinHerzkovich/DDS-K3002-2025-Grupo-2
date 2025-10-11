package aplicacion.domain.facts;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.HashCodeExclude;

@Entity
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class FactSolicitud {
    @Id
    private String nombreEstado;
    @HashCodeExclude
    private Long cantidadDeSolicitudes;

    @Override
    public String toString() {
        return "(" + nombreEstado + " - " + cantidadDeSolicitudes + ")";
    }
}
