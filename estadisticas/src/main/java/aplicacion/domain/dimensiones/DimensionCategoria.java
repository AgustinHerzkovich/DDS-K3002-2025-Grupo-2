package aplicacion.domain.dimensiones;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class DimensionCategoria {
    private Long idCategoria;
    private String nombre;
}
