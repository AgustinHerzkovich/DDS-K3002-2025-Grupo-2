package aplicacion.repositorios;

import aplicacion.domain.conexiones.Conexion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDeConexiones extends JpaRepository<Conexion, String> {
}
