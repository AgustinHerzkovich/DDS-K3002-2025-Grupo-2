package domain.repositorios;

import domain.solicitudes.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;

// REPOSITORIO DE SOLICITUDES
public interface RepositorioDeSolicitudes extends JpaRepository<SolicitudEliminacion, Long> {
}
