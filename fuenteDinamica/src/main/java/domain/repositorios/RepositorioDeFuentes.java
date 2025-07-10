package domain.repositorios;

import domain.fuentesDinamicas.FuenteDinamica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDeFuentes extends JpaRepository<FuenteDinamica, Long> {
}
