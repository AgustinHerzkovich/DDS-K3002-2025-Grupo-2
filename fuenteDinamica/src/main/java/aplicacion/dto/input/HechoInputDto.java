package aplicacion.dto.input;

import aplicacion.domain.hechos.Categoria;
import aplicacion.domain.hechos.Origen;
import aplicacion.domain.hechos.Ubicacion;
import aplicacion.domain.hechos.multimedias.Multimedia;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class HechoInputDto {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private Origen origen;
    private String contenidoTexto;
    private List<Multimedia> contenidoMultimedia;
    private Boolean anonimato;
    private Long contribuyenteId;
}
