package aplicacion.dtos.input;



import aplicacion.dtos.Origen;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class HechoInputDto {
    private String titulo;
    private String descripcion;
    private CategoriaInputDto categoria;
    private UbicacionInputDto ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private Origen origen;
    private String contenidoTexto;
    private List<MultimediaInputDto> contenidoMultimedia;
    private Boolean anonimato;
    private Long contribuyenteId;
}
