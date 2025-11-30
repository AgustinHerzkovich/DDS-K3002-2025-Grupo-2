package aplicacion.repositories;

import aplicacion.domain.usuarios.Contribuyente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContribuyenteRepository extends JpaRepository<Contribuyente, String> {
    boolean existsByMail(String mail);

    Optional<Contribuyente> findByMail(String mail);
}
