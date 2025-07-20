package domain.repositorios;

import java.util.List;
import domain.hechos.Hecho;
import domain.solicitudes.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioDeSolicitudes extends JpaRepository<SolicitudEliminacion, Long> {
    List<SolicitudEliminacion> findByHecho(Hecho hecho);
}