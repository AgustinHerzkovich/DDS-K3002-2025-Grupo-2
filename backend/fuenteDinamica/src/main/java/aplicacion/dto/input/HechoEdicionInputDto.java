package aplicacion.dto.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // Los atributos nulos no se envían ni se usan
public class HechoEdicionInputDto {
    private String titulo;
    private String descripcion;
    private CategoriaInputDto categoria;
    private UbicacionInputDto ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private String contenidoTexto;
    private List<MultimediaInputDto> contenidoMultimedia;
}
