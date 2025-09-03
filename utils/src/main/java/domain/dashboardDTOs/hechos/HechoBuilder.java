package domain.dashboardDTOs.hechos;


import domain.dashboardDTOs.usuarios.ContribuyenteDTO;
import domain.dashboardDTOs.usuarios.IdentidadDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HechoBuilder {
    private final HechoDTO hechoDTO = new HechoDTO();

    public HechoBuilder setTitulo(String titulo) {
        hechoDTO.setTitulo(titulo);
        return this;
    }

    public HechoBuilder setDescripcion(String descripcion) {
        hechoDTO.setDescripcion(descripcion);
        return this;
    }

    public HechoBuilder setCategoria(String nombreCategoria) {
        hechoDTO.setCategoria(new CategoriaDTO(nombreCategoria));
        return this;
    }

    public HechoBuilder setUbicacion(double latitud, double longitud) {
        hechoDTO.setUbicacion(new UbicacionDTO(latitud, longitud));
        return this;
    }

    public HechoBuilder setFechaAcontecimiento(LocalDateTime fecha) {
        hechoDTO.setFechaAcontecimiento(fecha);
        return this;
    }

    public HechoBuilder setOrigen() {
        hechoDTO.setOrigen("CARGA_MANUAL");
        return this;
    }

    public HechoBuilder setContenidoTexto(String texto) {
        hechoDTO.setContenidoTexto(texto);
        return this;
    }

    public HechoBuilder addContenidoMultimedia(String tipo, String formato, int tamanio, String resolucion, int duracion) {
        hechoDTO.getContenidoMultimedia().add(
                new ContenidoMultimediaDTO(tipo, formato, tamanio, resolucion, duracion)
        );
        return this;
    }

    public HechoBuilder setAnonimato(boolean anonimato) {
        hechoDTO.setAnonimato(anonimato);
        return this;
    }

    public HechoBuilder setAutor(String nombre, String apellido, LocalDate fechaNacimiento, Integer contribuyenteId, boolean esAdministrador) {
        ContribuyenteDTO contribuyente = new ContribuyenteDTO(contribuyenteId, esAdministrador);
        IdentidadDTO autor = new IdentidadDTO(nombre, apellido, fechaNacimiento, contribuyente, null);
        hechoDTO.setAutor(autor);
        return this;
    }

    public HechoDTO build() {
        return hechoDTO;
    }
}
