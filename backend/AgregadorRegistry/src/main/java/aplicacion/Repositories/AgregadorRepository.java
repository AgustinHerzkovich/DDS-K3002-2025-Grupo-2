package aplicacion.Repositories;

import aplicacion.domain.Agregador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Repository;

@Repository
public interface AgregadorRepository extends JpaRepository<Agregador, String>{

//TODO: investigar utilizar redis como base de datos key-value pero con persistencia

}
