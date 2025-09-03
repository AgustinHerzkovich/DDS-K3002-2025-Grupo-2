package aplicacion.dto.output;


import aplicacion.domain.hechos.Categoria;
import aplicacion.domain.hechos.Origen;
import aplicacion.domain.hechos.Ubicacion;
import aplicacion.domain.hechos.multimedias.Multimedia;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class HechoOutputDTO {
    private String id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private LocalDateTime fechaUltimaModificacion;
    private Origen origen;
    private String contenidoTexto;
    private List<Multimedia> contenidoMultimedia;
    private Boolean anonimato;
    private IdentidadContribuyente autor;
}