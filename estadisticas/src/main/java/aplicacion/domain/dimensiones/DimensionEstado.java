package aplicacion.domain.dimensiones;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class DimensionEstado {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long idEstado;
    private String nombreEstado;
}
