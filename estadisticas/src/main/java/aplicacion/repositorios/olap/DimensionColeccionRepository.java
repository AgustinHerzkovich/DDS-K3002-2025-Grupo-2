package aplicacion.repositorios.olap;

import aplicacion.domain.dimensiones.DimensionColeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DimensionColeccionRepository extends JpaRepository<DimensionColeccion, String> {
}
