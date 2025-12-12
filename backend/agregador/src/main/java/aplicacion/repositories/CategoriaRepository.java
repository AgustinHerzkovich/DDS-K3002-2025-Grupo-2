package aplicacion.repositories;

import aplicacion.domain.hechos.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("""
        SELECT c
        FROM Categoria c
        WHERE c.nombre = :nombre
    """)
    Optional<Categoria> findByNombre(@Param("nombre") String nombre);

    @Query(value = """
        SELECT c.nombre
        FROM categoria c
        WHERE (:textoLibre IS NULL
               OR MATCH(c.nombre) AGAINST(CONCAT(:textoLibre, '*') IN BOOLEAN MODE))
        ORDER BY MATCH(c.nombre) AGAINST(CONCAT(:textoLibre, '*') IN BOOLEAN MODE) DESC
        LIMIT :limit
        """,
            nativeQuery = true)
    List<String> findAutocompletado(
            @Param("textoLibre") String textoLibre,
            @Param("limit") int limit
    );

    @Query(value = """
        SELECT
            c.nombre
        FROM categoria c
        WHERE c.nombre LIKE CONCAT(:textoLibre, '%')
        ORDER BY c.nombre
        LIMIT :limit
        """, nativeQuery = true)
    List<String> findAutocompletadoLike(
            @Param("textoLibre") String textoLibre,
            @Param("limit") int limit
    );
}
