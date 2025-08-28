package aplicacion.repositorios;

import aplicacion.domain.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// REPOSITORIO DE HECHOS
@Repository
public interface RepositorioDeHechos extends JpaRepository<Hecho, String> {
}
