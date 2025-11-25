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

    @Query("""
        SELECT s
        FROM SolicitudEliminacion s
        ORDER BY 
            CASE 
                WHEN s.estado.class = EstadoSolicitudPendiente THEN 0
                ELSE 1
            END,
            s.fechaSubida DESC
    """)
    Page<SolicitudEliminacion> findAllOrderByPendienteFirst(Pageable pageable);
}