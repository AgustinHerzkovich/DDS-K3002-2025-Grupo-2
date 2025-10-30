package aplicacion.dto.input;

import aplicacion.dto.Origen;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HechoInputDto {
    private String titulo;
    private String descripcion;
    private CategoriaInputDto categoria;
    private UbicacionInputDto ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private Origen origen;
    private String contenidoTexto;
    private Boolean anonimato;
    private List<MultimediaInputDto> contenidoMultimedia;
    private ContribuyenteInputDto autor;
    private List<EtiquetaInputDto> etiquetas;
}
