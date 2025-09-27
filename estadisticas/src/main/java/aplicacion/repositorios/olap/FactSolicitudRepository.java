package aplicacion.repositorios.olap;

import aplicacion.domain.facts.FactSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface FactSolicitudRepository extends JpaRepository<FactSolicitud, String> {
    @Query(
            value = """
            SELECT count(*) 
            FROM fact_solicitud
            WHERE estado = 'Spam'
            """,
            nativeQuery = true
    )
    Long obtenerCantidadSolicitudesSpam();
    @Query(
            value = """
            SELECT count(*) 
            FROM fact_solicitud
            """,
            nativeQuery = true
    )
    Long obtenerCantidadSolicitudesTotal();
}
