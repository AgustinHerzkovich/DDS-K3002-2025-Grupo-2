package aplicacion.services.dto;

import domain.hechos.Categoria;
import domain.hechos.Etiqueta;
import domain.hechos.Origen;
import domain.hechos.Ubicacion;
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
