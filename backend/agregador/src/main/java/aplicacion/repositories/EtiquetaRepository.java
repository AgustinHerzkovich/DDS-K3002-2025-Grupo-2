package aplicacion.repositories;

import aplicacion.domain.hechos.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {
    @Query("SELECT e FROM Etiqueta e WHERE e.nombre = :nombre")
    Optional<Etiqueta> findByNombre(@Param("nombre") String nombre);

    @Query(value = """
        SELECT e.nombre
        FROM etiqueta e
        WHERE (:textoLibre IS NULL
               OR MATCH(e.nombre) AGAINST(CONCAT(:textoLibre, '*') IN BOOLEAN MODE))
        ORDER BY MATCH(e.nombre) AGAINST(CONCAT(:textoLibre, '*') IN BOOLEAN MODE) DESC
        LIMIT :limit
        """,
            nativeQuery = true)
    List<String> findAutocompletado(
            @Param("textoLibre") String textoLibre,
            @Param("limit") int limit
    );

    @Query(value = """
        SELECT
            e.nombre
        FROM etiqueta e
        WHERE e.nombre LIKE CONCAT(:textoLibre, '%')
        ORDER BY e.nombre
        LIMIT :limit
        """, nativeQuery = true)
    List<String> findAutocompletadoLike(
            @Param("textoLibre") String textoLibre,
            @Param("limit") int limit
    );
}
