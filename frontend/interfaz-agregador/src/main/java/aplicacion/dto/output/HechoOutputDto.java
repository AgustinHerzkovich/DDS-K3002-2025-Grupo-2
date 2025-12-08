package aplicacion.dto.output;

import aplicacion.dto.Origen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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
    private Boolean visible;
    private String direccion; // Dirección completa calculada con geocoding

    @Override
    public String toString(){
        return "HechoOutputDto{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", categoria=" + categoria +
                ", ubicacion=" + ubicacion +
                ", fechaAcontecimiento=" + fechaAcontecimiento +
                ", fechaCarga=" + fechaCarga +
                ", fechaUltimaModificacion=" + fechaUltimaModificacion +
                ", origen=" + origen +
                ", contenidoTexto='" + contenidoTexto + '\'' +
                ", contenidoMultimedia=" + contenidoMultimedia +
                ", anonimato=" + anonimato +
                ", autor=" + autor +
                ", etiquetas=" + etiquetas +
                ", visible=" + visible +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}