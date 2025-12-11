package aplicacion.repositories;

import java.util.List;
import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.solicitudes.SolicitudEliminacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudEliminacion, Long> {
    List<SolicitudEliminacion> findByHecho(Hecho hecho);

    @Query(value = """
        SELECT s.*
        FROM solicitud_eliminacion s
        JOIN estado_solicitud e ON e.id = s.estado_id
        WHERE s.fecha_subida >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)
        ORDER BY
            CASE
                WHEN e.dtype = 'EstadoSolicitudPendiente' THEN 0
                ELSE 1
            END,
            s.fecha_subida DESC
    """,
    countQuery = """
        SELECT COUNT(s.id)
        FROM solicitud_eliminacion s
        WHERE s.fecha_subida >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)
    """,
    nativeQuery = true)
    Page<SolicitudEliminacion> findAllOrderByPendienteFirst(Pageable pageable);
}