package aplicacion.repositorios.olap;

import aplicacion.domain.facts.FactColeccion;
import aplicacion.domain.id.FactColeccionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactColeccionRepository extends JpaRepository<FactColeccion, FactColeccionId> {
}
