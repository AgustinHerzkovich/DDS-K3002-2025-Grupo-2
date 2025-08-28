package aplicacion.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aplicacion.FuenteProxy;

@Repository
public interface RepositorioDeFuentesProxy extends JpaRepository<FuenteProxy, Long>{
    // Este repositorio se usa para manejar los hechos que son fuentes proxy.
    // No se necesita implementar métodos adicionales por ahora, ya que JpaRepository
    // proporciona las operaciones CRUD básicas.
}
