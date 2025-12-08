package aplicacion.repositories.olap;

import aplicacion.domain.facts.FactSolicitud;
import aplicacion.dtos.CantidadSolicitudesPorTipo;
import aplicacion.dtos.CantidadSolicitudesPorTipoInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FactSolicitudRepository extends JpaRepository<FactSolicitud, String> {
    @Query(
            value = """
            SELECT nombreEstado as tipoDeSolicitud, cantidadDeSolicitudes as cantidadSolicitudes 
            FROM FactSolicitud
            """,
            nativeQuery = true)
    List<CantidadSolicitudesPorTipoInterface> obtenerCantidadDeSolicitudesPorTipo();
}
