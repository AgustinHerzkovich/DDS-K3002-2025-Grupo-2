package aplicacion.services.dto;

import aplicacion.domain.hechos.Categoria;
import aplicacion.domain.hechos.Origen;
import aplicacion.domain.hechos.Ubicacion;
import aplicacion.domain.hechos.multimedias.Multimedia;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // Excluir campos nulos en la serialización JSON
public class HechoDTO {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private Origen origen;
    private String contenidoTexto;
    private List<Multimedia> contenidoMultimedia;
    private Boolean anonimato;
    private Long identidadId;
}
