package aplicacion.dto.input;

import aplicacion.domain.hechos.Categoria;
import aplicacion.domain.hechos.Origen;
import aplicacion.domain.hechos.Ubicacion;
import aplicacion.domain.hechos.multimedias.Multimedia;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import domain.dashboardDTOs.hechos.CategoriaDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class HechoInputDTO {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private Origen origen;
    private String contenidoTexto;
    private Boolean anonimato;
    private List<Multimedia> contenidoMultimedia;
    private IdentidadContribuyente autor;
    public HechoInputDTO() {
    }

}
