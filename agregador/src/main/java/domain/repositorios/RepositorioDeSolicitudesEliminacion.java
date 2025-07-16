package domain.repositorios;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RepositorioDeSolicitudesEliminacion extends JpaRepository{
    @Modifying
    @Transactional
    @Query("")
    void update(@Param("uuid") String uuid, @Param("consensuado") Boolean consensuado);

}
