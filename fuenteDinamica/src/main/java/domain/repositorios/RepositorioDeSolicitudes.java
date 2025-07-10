package domain.repositorios;

import domain.solicitudes.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// REPOSITORIO DE SOLICITUDES
@Repository
public interface RepositorioDeSolicitudes extends JpaRepository<SolicitudEliminacion, Long> {
}
