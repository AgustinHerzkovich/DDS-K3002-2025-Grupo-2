package aplicacion.repositories.olap;

import aplicacion.domain.dimensiones.DimensionColeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DimensionColeccionRepository extends JpaRepository<DimensionColeccion, String> {

    List<DimensionColeccion> findByIdColeccionAgregadorIn(Set<String> strings);
}
