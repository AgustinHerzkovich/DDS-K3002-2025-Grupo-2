package aplicacion.dtos.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
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
