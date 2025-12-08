package aplicacion.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aplicacion.domain.fuentesProxy.FuenteProxy;

@Repository
public interface FuenteProxyRepository extends JpaRepository<FuenteProxy, String>{
    // Este repositorio se usa para manejar los hechos que son fuentes proxy.
    // No se necesita implementar métodos adicionales por ahora, ya que JpaRepository
    // proporciona las operaciones CRUD básicas.
}
