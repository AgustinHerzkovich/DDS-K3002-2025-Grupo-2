package aplicacion.dto.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Los atributos nulos no se envían ni se usan
public class HechoEdicionInputDto {
    @Size(max = 200, message = "El título no puede tener más de 200 caracteres")
    private String titulo;
    @Size(max = 1000, message = "La descripción no puede tener más de 1000 caracteres")
    private String descripcion;
    private CategoriaInputDto categoria;
    private UbicacionInputDto ubicacion;
    private LocalDateTime fechaAcontecimiento;
    @Size(max = 500, message = "El contenido de texto no puede tener más de 500 caracteres")
    private String contenidoTexto;
    private List<MultimediaInputDto> contenidoMultimedia;
}
