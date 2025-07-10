package domain.repositorios;

import domain.fuentesDinamicas.FuenteDinamica;
import domain.fuentesDinamicas.HechoXFuente;
import domain.fuentesDinamicas.HechoXFuenteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositorioDeHechosXFuente extends JpaRepository<HechoXFuente, HechoXFuenteId> {
    Optional<HechoXFuente> findByFuente(FuenteDinamica fuente);
}