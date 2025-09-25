package aplicacion.repositorios.olap;

import aplicacion.domain.facts.FactHecho;
import aplicacion.domain.hechosYSolicitudes.Hecho;
import aplicacion.domain.id.FactHechoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface FactHechoRepository extends JpaRepository<FactHecho, FactHechoId> {

}
