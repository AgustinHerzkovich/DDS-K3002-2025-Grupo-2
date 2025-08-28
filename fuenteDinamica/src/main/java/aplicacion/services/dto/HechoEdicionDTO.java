package aplicacion.services.dto;

import aplicacion.domain.hechos.Categoria;
import aplicacion.domain.hechos.Ubicacion;
import aplicacion.domain.hechos.multimedias.Multimedia;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class HechoEdicionDTO {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private String contenidoTexto;
    private List<Multimedia> contenidoMultimedia;
}
