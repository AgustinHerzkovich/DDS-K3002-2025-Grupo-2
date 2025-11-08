package aplicacion.repositorios;

import aplicacion.domain.colecciones.Coleccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RepositorioDeColecciones extends JpaRepository<Coleccion, String> {
    @Query("""
    SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END
    FROM Coleccion c
    JOIN c.fuentes f
    WHERE f.id = :fuenteId
    """)
    Boolean existsByFuenteId(@Param("fuenteId") String fuenteId);

    @Query(value = "SELECT * FROM coleccion " +
            "WHERE MATCH(titulo, descripcion) " +
            "AGAINST(CONCAT(:textoLibre, '*') IN BOOLEAN MODE)",
            countQuery = "SELECT COUNT(*) FROM coleccion " +
                    "WHERE MATCH(titulo, descripcion) " +
                    "AGAINST(CONCAT(:textoLibre, '*') IN BOOLEAN MODE)",
            nativeQuery = true)
    Page<Coleccion> findByTextoLibre(@Param("textoLibre") String textoLibre, Pageable pageable);

    @Query(value = "SELECT titulo FROM coleccion " +
            "WHERE MATCH(titulo) " +
            "AGAINST(CONCAT(:textoLibre, '*') IN BOOLEAN MODE)"+
            "LIMIT :limit",
            nativeQuery = true)
    List<String> findAutocompletado(@Param("textoLibre") String textoLibre, @Param("limit")  Integer limit);
    @Query(value = "SELECT titulo FROM coleccion " +
            "WHERE titulo LIKE CONCAT('%', :textoLibre, '%') " +
            "ORDER BY " +
            "  CASE " +
            "    WHEN titulo LIKE :textoLibre THEN 1 " +
            "    WHEN titulo LIKE CONCAT(:textoLibre, '%') THEN 2 " +
            "    WHEN titulo LIKE CONCAT('% ', :textoLibre, '%') THEN 3 " +
            "    WHEN titulo LIKE CONCAT('%', :textoLibre, ' %') THEN 4 " +
            "    ELSE 5 " +
            "  END, titulo ASC " +
            "LIMIT :limit",
            nativeQuery = true)
    List<String> findAutocompletadoLike(@Param("textoLibre")String textoLibre, @Param("limit") Integer limit);
}
