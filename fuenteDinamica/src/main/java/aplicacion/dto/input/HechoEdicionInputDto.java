package aplicacion.dto.input;

import aplicacion.domain.hechos.Categoria;
import aplicacion.domain.hechos.Ubicacion;
import aplicacion.domain.hechos.multimedias.Multimedia;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // Los atributos nulos no se envían ni se usan
public class HechoEdicionInputDto {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private String contenidoTexto;
    private List<Multimedia> contenidoMultimedia;
}
