package aplicacion.dto.output;

import aplicacion.domain.hechos.Categoria;
import aplicacion.domain.hechos.EstadoRevision;
import aplicacion.domain.hechos.Origen;
import aplicacion.domain.hechos.Ubicacion;
import aplicacion.domain.hechos.multimedias.Multimedia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class HechoRevisadoOutputDto {
    private String id;
    private String titulo;
    private String descripcion;
    private CategoriaOutputDto categoria;
    private UbicacionOutputDto ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private LocalDateTime fechaUltimaModificacion;
    private Origen origen;
    private String contenidoTexto;
    private List<MultimediaOutputDto> contenidoMultimedia;
    private Boolean anonimato;
    private ContribuyenteOutputDto autor;
    private EstadoRevision estadoRevision;
    private String sugerencia;
}
