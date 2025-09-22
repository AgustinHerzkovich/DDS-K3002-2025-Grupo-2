package aplicacion.repositorios.olap;

import aplicacion.domain.facts.FactHecho;
import aplicacion.domain.id.FactHechoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactHechoRepository extends JpaRepository<FactHecho, FactHechoId> {
}
