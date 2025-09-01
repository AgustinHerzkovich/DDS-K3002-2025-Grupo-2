package domain.dashboardDTOs.hechos;

import domain.dashboardDTOs.usuarios.IdentidadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private List<ContenidoMultimediaDTO> contenidoMultimedia = new ArrayList<>();
    private boolean anonimato;
    private IdentidadDTO autor;

    @Override
    public String toString() {
        return "===HECHO===\nTítulo: " + titulo +
                " \n Descripción: " + descripcion +
                " \n Categoría: " + (categoria != null ? categoria.getNombre() : "null") +
                " \n Ubicación: " + (ubicacion != null ? ubicacion.getLatitud() + ", " + ubicacion.getLongitud() : "null") +
                " \n Fecha Acontecimiento: " + fechaAcontecimiento +
                " \n Origen: " + origen +
                " \n Texto: " + contenidoTexto +
                " \n Multimedia: " + (contenidoMultimedia != null ? contenidoMultimedia.size() + " archivo(s)" : "0 archivo(s)") +
                " \n Anónimo: " + anonimato +
                " \n Autor: " + (autor != null ? autor.getNombre() + " " + autor.getApellido() : "null") +
                " \n Contribuyente ID: " + (autor != null && autor.getContribuyente() != null ? autor.getContribuyente().getContribuyenteId() : "null") +
                " \n Es administrador: " + (autor != null && autor.getContribuyente() != null ? autor.getContribuyente().isEsAdministrador() : "null");
    }

}