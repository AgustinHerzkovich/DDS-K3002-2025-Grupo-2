package aplicacion.repositorios;

import aplicacion.domain.agregadorID.AgregadorID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgregadorIDRepository extends JpaRepository<AgregadorID, Long> {

}
