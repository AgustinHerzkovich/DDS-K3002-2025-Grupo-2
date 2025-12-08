package aplicacion.repositories.olap;

import aplicacion.utils.ConfiguracionGlobal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionGlobalRepository extends JpaRepository<ConfiguracionGlobal, String> {
}
