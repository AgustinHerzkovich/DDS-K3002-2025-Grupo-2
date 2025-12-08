package aplicacion.repositories;

import aplicacion.domain.hechos.EstadoRevision;
import aplicacion.domain.hechos.Hecho;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

// REPOSITORIO DE HECHOS
@Repository
public interface HechoRepository extends JpaRepository<Hecho, String> {

    Page<Hecho> findByAutorId(String contribuyenteId, Pageable pageable);

    Page<Hecho> findByEstadoRevision(EstadoRevision estadoRevision, Pageable pageable);

    List<Hecho> findByEstadoRevision(EstadoRevision estadoRevision);

    Page<Hecho> findByEstadoRevisionAndFechaUltimaModificacionAfter(
        EstadoRevision estadoRevision,
        LocalDateTime fecha,
        Pageable pageable
    );

    List<Hecho> findByEstadoRevisionAndFechaUltimaModificacionAfter(
            EstadoRevision estadoRevision,
            LocalDateTime fecha
    );
}
