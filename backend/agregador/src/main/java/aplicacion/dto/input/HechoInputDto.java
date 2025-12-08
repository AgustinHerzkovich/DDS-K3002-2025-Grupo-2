package aplicacion.dto.input;

import aplicacion.domain.hechos.Origen;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HechoInputDto {
    private String id; // Conservo el id que me llega de otra fuente
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
}
