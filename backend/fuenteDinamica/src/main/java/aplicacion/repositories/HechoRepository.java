package aplicacion.repositories;

import aplicacion.domain.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// REPOSITORIO DE HECHOS
@Repository
public interface HechoRepository extends JpaRepository<Hecho, String> {

    List<Hecho> findByAutorId(String contribuyenteId);
}
