package domain.dashboardDTOs;


import java.time.LocalDateTime;

public class HechoPostBuilder {
    private final HechoPostDTO hechoDTO = new HechoPostDTO();

    public HechoPostBuilder setTitulo(String titulo) {
        hechoDTO.setTitulo(titulo);
        return this;
    }

    public HechoPostBuilder setDescripcion(String descripcion) {
        hechoDTO.setDescripcion(descripcion);
        return this;
    }

    public HechoPostBuilder setCategoria(String nombreCategoria) {
        hechoDTO.setCategoria(new CategoriaDTO(nombreCategoria));
        return this;
    }

    public HechoPostBuilder setUbicacion(double latitud, double longitud) {
        hechoDTO.setUbicacion(new UbicacionDTO(latitud, longitud));
        return this;
    }

    public HechoPostBuilder setFechaAcontecimiento(LocalDateTime fecha) {
        hechoDTO.setFechaAcontecimiento(fecha);
        return this;
    }

    public HechoPostBuilder setContenidoTexto(String texto) {
        hechoDTO.setContenidoTexto(texto);
        return this;
    }

    public HechoPostBuilder addContenidoMultimedia(String tipo, String formato, int tamanio, String resolucion, int duracion) {
        hechoDTO.getContenidoMultimedia().add(
                new ContenidoMultimediaDTO(tipo, formato, tamanio, resolucion, duracion)
        );
        return this;
    }

    public HechoPostBuilder setAnonimato(boolean anonimato) {
        hechoDTO.setAnonimato(anonimato);
        return this;
    }

    public HechoPostBuilder setAutor(Integer idAutor) {
        hechoDTO.setContribuyenteId(idAutor);
        return this;
    }

    public HechoPostDTO build() {
        return hechoDTO;
    }
}
