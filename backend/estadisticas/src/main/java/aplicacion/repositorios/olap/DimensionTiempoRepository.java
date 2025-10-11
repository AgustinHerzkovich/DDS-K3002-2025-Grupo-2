package aplicacion.repositorios.olap;

import aplicacion.domain.dimensiones.DimensionCategoria;
import aplicacion.domain.dimensiones.DimensionTiempo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DimensionTiempoRepository extends JpaRepository<DimensionTiempo, Long> {
    @Query(
            value = """
                        SELECT * FROM DimensionTiempo dt
                        WHERE CONCAT_WS('|', dt.anio, dt.hora) IN (:codigos)""",
            nativeQuery = true
    )
    List<DimensionTiempo> findByTiempo(Set<String> codigos);
}
