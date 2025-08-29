package aplicacion.services.dto;

import aplicacion.domain.hechos.Categoria;
import aplicacion.domain.hechos.Etiqueta;
import aplicacion.domain.hechos.Origen;
import aplicacion.domain.hechos.Ubicacion;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class HechoInEstaticaDTO {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private Origen origen;
    private Boolean visible;
    private List<Etiqueta> etiquetas;
}
