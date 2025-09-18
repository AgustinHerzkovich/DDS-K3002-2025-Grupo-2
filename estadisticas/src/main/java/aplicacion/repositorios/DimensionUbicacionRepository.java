package aplicacion.repositorios;

import aplicacion.domain.dimensiones.DimensionUbicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DimensionUbicacionRepository extends JpaRepository<DimensionUbicacion, Long> {
}
