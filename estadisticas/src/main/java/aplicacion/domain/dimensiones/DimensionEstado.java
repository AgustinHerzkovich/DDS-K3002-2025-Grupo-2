package aplicacion.domain.dimensiones;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeExclude;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@EqualsAndHashCode

public class DimensionEstado {
    @HashCodeExclude
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long idEstado;
    private String nombreEstado;
}
