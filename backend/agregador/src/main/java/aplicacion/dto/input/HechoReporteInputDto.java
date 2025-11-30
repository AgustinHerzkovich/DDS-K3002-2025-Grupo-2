package aplicacion.dto.input;

import aplicacion.domain.hechos.Origen;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

// Este se utiliza para el endpoint de reportar hecho, ya que en este caso, el autor ya existe, por ende se manda solo su id
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HechoReporteInputDto {
    @Size(max = 200, message = "El título no puede tener más de 200 caracteres")
    private String titulo;
    @Size(max = 1000, message = "La descripción no puede tener más de 1000 caracteres")
    private String descripcion;
    private CategoriaInputDto categoria;
    private UbicacionInputDto ubicacion;
    private LocalDateTime fechaAcontecimiento;
    @Size(max = 255, message = "El origen no puede tener más de 255 caracteres")
    private Origen origen;
    @Size(max = 500, message = "El contenido de texto no puede tener más de 500 caracteres")
    private String contenidoTexto;
    private Boolean anonimato;
    private List<MultimediaInputDto> contenidoMultimedia;
    private String autor;
}
