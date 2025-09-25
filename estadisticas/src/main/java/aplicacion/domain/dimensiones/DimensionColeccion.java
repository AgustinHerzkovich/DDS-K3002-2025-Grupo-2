package aplicacion.domain.dimensiones;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class DimensionColeccion {
    @Id
    private String idColeccion;
    private String titulo;
    private String descripcion;
}
