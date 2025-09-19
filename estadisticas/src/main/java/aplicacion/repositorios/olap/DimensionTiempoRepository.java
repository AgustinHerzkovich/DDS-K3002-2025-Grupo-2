package aplicacion.repositorios.olap;

import aplicacion.domain.dimensiones.DimensionTiempo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DimensionTiempoRepository extends JpaRepository<DimensionTiempo, Long> {
}
