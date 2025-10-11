package aplicacion.repositorios.agregador;

import aplicacion.domain.hechosYSolicitudes.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColeccionRepository extends JpaRepository<Coleccion, String> {
}
