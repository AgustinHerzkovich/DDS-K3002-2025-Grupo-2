package aplicacion.domain.facts;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FactColeccion {
    @Id
    private String idColeccion;
    private Long idubicacion;
    private Long idTiempo;
    private Long idCategoria;
    private Long cantidadHechos;
}
