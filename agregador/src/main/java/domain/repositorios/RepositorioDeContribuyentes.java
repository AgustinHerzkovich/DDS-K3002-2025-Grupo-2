package domain.repositorios;

import domain.usuarios.Contribuyente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDeContribuyentes extends JpaRepository<Contribuyente, Long> {
}
