package aplicacion.repositorios;

import aplicacion.domain.colecciones.Coleccion;
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
            "AGAINST(:textoLibre IN NATURAL LANGUAGE MODE)",
            nativeQuery = true)
    List<Coleccion> findByTextoLibre(@Param("textoLibre") String textoLibre);
}
