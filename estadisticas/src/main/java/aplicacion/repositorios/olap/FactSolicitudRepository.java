package aplicacion.repositorios.olap;

import aplicacion.domain.facts.FactSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface FactSolicitudRepository extends JpaRepository<FactSolicitud, String> {
}
