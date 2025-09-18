package aplicacion.domain.dimensiones;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;

@Getter
@Setter
@Entity
public class DimensionUbicacion {
    @Id
    private Integer id;
    private String pais = "Argentina";
    private String provincia;
}
