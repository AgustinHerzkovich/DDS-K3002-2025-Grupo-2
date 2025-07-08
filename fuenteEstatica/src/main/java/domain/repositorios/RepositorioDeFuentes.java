package domain.repositorios;

import domain.fuentesEstaticas.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDeFuentes extends JpaRepository<Fuente, Long> {
}
