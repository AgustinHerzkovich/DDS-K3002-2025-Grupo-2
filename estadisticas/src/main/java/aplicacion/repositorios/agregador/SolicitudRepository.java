package aplicacion.repositorios.agregador;

import aplicacion.domain.hechosYSolicitudes.solicitudes.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudRepository extends JpaRepository<SolicitudEliminacion, Long> {
}
