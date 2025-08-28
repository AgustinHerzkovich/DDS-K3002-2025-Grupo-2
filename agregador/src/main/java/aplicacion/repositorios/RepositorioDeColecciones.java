package aplicacion.repositorios;

import aplicacion.domain.colecciones.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RepositorioDeColecciones extends JpaRepository<Coleccion, String> {

}
