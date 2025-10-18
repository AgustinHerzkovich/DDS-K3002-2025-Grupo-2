package aplicacion.repositorios;

import aplicacion.domain.hechos.Categoria;
import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.hechos.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioDeHechos extends JpaRepository<Hecho, String> {
    @Query("""
        SELECT h
        FROM Hecho h
        JOIN HechoXColeccion hc ON h.id = hc.hecho.id
        WHERE hc.coleccion.id = :idColeccion
    """)
    List<Hecho> findByCollectionId(@Param("idColeccion") String idColeccion);

    @Query(value = "SELECT h.* FROM hecho h " +
            "JOIN hecho_coleccion hc ON h.id = hc.hecho_id " +
            "WHERE hc.coleccion_id = :idColeccion " +
            "AND MATCH(h.titulo, h.descripcion, h.contenido_texto) " +
            "AGAINST(:textoLibre IN NATURAL LANGUAGE MODE)",
            nativeQuery = true)
    List<Hecho> findByCollectionIdAndTextoLibre(@Param("idColeccion") String idColeccion, @Param("textoLibre") String textoLibre);

    @Query("""
        SELECT h
        FROM Hecho h
        JOIN HechoXColeccion hc ON h.id = hc.hecho.id
        WHERE hc.coleccion.id = :idColeccion AND hc.consensuado = true
    """)
    List<Hecho> findCuredByCollectionId(@Param("idColeccion") String idColeccion);

    @Query(value = "SELECT h.* FROM hecho h " +
            "JOIN hecho_coleccion hc ON h.id = hc.hecho_id " +
            "WHERE hc.coleccion_id = :idColeccion " +
            "AND hc.consensuado = true " +
            "AND MATCH(h.titulo, h.descripcion, h.contenido_texto) " +
            "AGAINST(:textoLibre IN NATURAL LANGUAGE MODE)",
            nativeQuery = true)
    List<Hecho> findCuredByCollectionIdAndTextoLibre(@Param("idColeccion") String idColeccion, @Param("textoLibre") String textoLibre);

    @Query("""
        SELECT h
        FROM Hecho h
        WHERE h.id = :idHecho
    """)
    Hecho findByHechoId(@Param("idHecho") String idHecho);

    @Query("""
        SELECT h
        FROM Hecho h
        WHERE h.titulo = :titulo
          AND h.descripcion = :descripcion
          AND h.categoria = :categoria
          AND h.ubicacion = :ubicacion
          AND h.fechaAcontecimiento = :fechaAcontecimiento
          AND h.contenidoTexto = :contenidoTexto
    """)
    Optional<Hecho> findDuplicado(@Param("titulo") String titulo,
                                  @Param("descripcion") String descripcion,
                                  @Param("categoria") Categoria categoria,
                                  @Param("ubicacion") Ubicacion ubicacion,
                                  @Param("fechaAcontecimiento") LocalDateTime fechaAcontecimiento,
                                  @Param("contenidoTexto") String contenidoTexto);
    @Query(
            value = "SELECT * FROM hecho h WHERE MD5(CONCAT_WS('|', " +
                    "titulo, descripcion, categoria_id, latitud, longitud, fecha_acontecimiento, IFNULL(contenido_texto, ''))) IN (:codigos)",
            nativeQuery = true
    )
    List<Hecho> findByCodigoHasheadoIn(List<String> codigos);

    @Query(value = "SELECT * FROM hecho " +
            "WHERE MATCH(titulo, descripcion, contenido_texto) " +
            "AGAINST(:textoLibre IN NATURAL LANGUAGE MODE)",
            nativeQuery = true)
    List<Hecho>findByTextoLibre(@Param("textoLibre") String textoLibre);

    List<Hecho> findByAutorId(Long autorId);
}