package aplicacion.repositories.agregador;

import aplicacion.domain.hechosYSolicitudes.solicitudes.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SolicitudRepository extends JpaRepository<SolicitudEliminacion, Long> {
    @Query(value = """ 
            SELECT es.dtype, count(*) FROM solicitud_eliminacion se
            INNER JOIN estado_solicitud es ON se.estado_id = es.id
            GROUP BY es.dtype """, nativeQuery = true)
    List<Object[]> cantidadesEstados();
}
