package domain.repositorios;

import domain.fuentesDinamicas.FuenteDinamica;
import domain.fuentesDinamicas.HechoXFuente;
import domain.fuentesDinamicas.HechoXFuenteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioDeHechosXFuente extends JpaRepository<HechoXFuente, HechoXFuenteId> {
    List<HechoXFuente> findByFuente(FuenteDinamica fuente);
}