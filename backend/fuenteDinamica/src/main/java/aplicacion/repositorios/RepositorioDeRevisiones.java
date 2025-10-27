package aplicacion.repositorios;

import aplicacion.domain.hechos.RevisionHecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDeRevisiones extends JpaRepository<RevisionHecho, Long> {
}
