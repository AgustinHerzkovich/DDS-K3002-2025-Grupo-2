package aplicacion.repositorios.olap;

import aplicacion.domain.dimensiones.DimensionCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DimensionCategoriaRepository extends JpaRepository<DimensionCategoria, String> {

}
