package aplicacion.repositories;

import aplicacion.domain.hechos.RevisionHecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevisionRepository extends JpaRepository<RevisionHecho, Long> {
}
