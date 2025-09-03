package aplicacion.dto.input;

import aplicacion.domain.algoritmos.AlgoritmoConsenso;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.criterios.CriterioDePertenencia;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ColeccionInputDTO {
    private String titulo;
    private String descripcion;
    private List<CriterioDePertenencia> criteriosDePertenencia;
    private List<Fuente> fuentes;
    private AlgoritmoConsenso algoritmoConsenso;

}
