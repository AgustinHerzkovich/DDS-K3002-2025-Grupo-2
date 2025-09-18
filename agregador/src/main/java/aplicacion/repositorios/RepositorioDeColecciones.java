package aplicacion.repositorios;

import aplicacion.domain.colecciones.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RepositorioDeColecciones extends JpaRepository<Coleccion, String> {
    @Query("""
    SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END
    FROM Coleccion c
    JOIN c.fuentes f
    WHERE f.id = :fuenteId
    """)
    Boolean existsByFuenteId(String fuenteId);
}
