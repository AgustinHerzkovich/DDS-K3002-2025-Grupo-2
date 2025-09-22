package aplicacion.repositorios.agregador;

import aplicacion.domain.hechosYSolicitudes.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HechoRepository extends JpaRepository<Hecho, String> {
}
