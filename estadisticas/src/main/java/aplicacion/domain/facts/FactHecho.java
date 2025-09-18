package aplicacion.domain.facts;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FactHecho {
    @Id
    private Long idUbicacion;
    private Long idCategoria;
    private Long idTiempo;
    private Long cantidadDeHechos;
}
