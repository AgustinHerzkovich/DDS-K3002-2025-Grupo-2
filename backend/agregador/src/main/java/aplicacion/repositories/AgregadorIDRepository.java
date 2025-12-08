package aplicacion.repositories;

import aplicacion.domain.agregadorID.AgregadorID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgregadorIDRepository extends JpaRepository<AgregadorID, Long> {

}
