package domain.repositorios;

import domain.solicitudes.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RepositorioDeSolicitudesEliminacion extends JpaRepository<SolicitudEliminacion, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE SolicitudEliminacion s SET s.estado = :estado WHERE s.id = :id")
    void update(@Param("id") Long id, @Param("estado") Boolean estado);
    //ESTO QUEDO RE DEPRECADO Y NO SE QUE QUISIERON HACER TODO

}
