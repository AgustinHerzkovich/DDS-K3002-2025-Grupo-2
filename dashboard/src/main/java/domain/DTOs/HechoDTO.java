package domain.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HechoDTO {
    private String titulo;
    private String descripcion;
    private CategoriaDTO categoria;
    private UbicacionDTO ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private String origen;
    private String contenidoTexto;
    private List<ContenidoMultimediaDTO> contenidoMultimedia;
    private boolean anonimato;
    private AutorDTO autor;
}