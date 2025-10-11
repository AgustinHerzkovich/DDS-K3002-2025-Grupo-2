package aplicacion.repositorios.olap;

import aplicacion.domain.facts.FactSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface FactSolicitudRepository extends JpaRepository<FactSolicitud, String> {
    @Query(
            value = """
            SELECT cantidadDeSolicitudes
            FROM FactSolicitud
            WHERE nombreEstado = 'EstadoSolicitudSpam'
            """,
            nativeQuery = true
    )
    Long obtenerCantidadSolicitudesSpam();
    @Query(
            value = """
            SELECT SUM(cantidadDeSolicitudes) 
            FROM FactSolicitud
            """,
            nativeQuery = true
    )
    Long obtenerCantidadSolicitudesTotal();
}
