package domain.repositorios;

import java.util.List;
import domain.hechos.Hecho;
import domain.solicitudes.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepositorioDeSolicitudes extends JpaRepository<SolicitudEliminacion, Long> {

    // TODO: Revisar la query
    @Query("""
        SELECT s
        FROM SolicitudEliminacion s
        WHERE s.id = :idHecho
    """)
    List<SolicitudEliminacion> findByHechoId(@Param("idHecho") String idHecho);

    // TODO: Revisar la query
    /*@Query("""
        SELECT s
        FROM SolicitudEliminacion s
        WHERE s.id = :idSolicitud
    """)
    SolicitudEliminacion findBySolictudId(@Param("idSolicitud") Long idSolicitud);
    */
}