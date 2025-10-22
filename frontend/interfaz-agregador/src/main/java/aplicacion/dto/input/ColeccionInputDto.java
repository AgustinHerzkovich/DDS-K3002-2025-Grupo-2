package aplicacion.dto.input;

import aplicacion.dto.TipoAlgoritmoConsenso;
import lombok.Getter;

import java.util.List;

@Getter
public class ColeccionInputDto {
    private String titulo;
    private String descripcion;
    private List<CriterioDePertenenciaInputDto> criteriosDePertenencia;
    private List<FuenteInputDto> fuentes;
    private TipoAlgoritmoConsenso tipoAlgoritmoConsenso;
}
