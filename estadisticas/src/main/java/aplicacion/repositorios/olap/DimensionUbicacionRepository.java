package aplicacion.repositorios.olap;

import aplicacion.domain.dimensiones.DimensionCategoria;
import aplicacion.domain.dimensiones.DimensionUbicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DimensionUbicacionRepository extends JpaRepository<DimensionUbicacion, Long> {
    @Query(
            value = """
                SELECT * FROM DimensionUbicacion dt
                WHERE CONCAT_WS('|', dt.pais, dt.provincia) IN (:codigos)""",
            nativeQuery = true
    )
    List<DimensionUbicacion> findByUbicaciones(List<String> codigos);
}
