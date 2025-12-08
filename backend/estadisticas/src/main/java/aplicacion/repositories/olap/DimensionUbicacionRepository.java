package aplicacion.repositories.olap;

import aplicacion.domain.dimensiones.DimensionUbicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DimensionUbicacionRepository extends JpaRepository<DimensionUbicacion, Long> {
    @Query(
            value = """
                SELECT * FROM DimensionUbicacion dt
                WHERE CONCAT_WS('|', dt.pais, dt.provincia) IN (:codigos)""",
            nativeQuery = true
    )
    List<DimensionUbicacion> findByUbicaciones(@Param("codigos") Set<String> codigos);
}
