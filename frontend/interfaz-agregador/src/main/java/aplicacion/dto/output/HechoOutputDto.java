package aplicacion.dto.output;

import aplicacion.dto.Origen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class HechoOutputDto {
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
    private List<EtiquetaOutputDto> etiquetas;
    private String direccion; // Dirección completa calculada con geocoding
}