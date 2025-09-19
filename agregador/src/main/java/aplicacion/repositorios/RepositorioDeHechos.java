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

    @Query("""
        SELECT h
        FROM Hecho h
        JOIN HechoXColeccion hc ON h.id = hc.hecho.id
        WHERE hc.coleccion.id = :idColeccion AND hc.consensuado = true
    """)
    List<Hecho> findCuredByCollectionId(@Param("idColeccion") String idColeccion);

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
}