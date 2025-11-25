package aplicacion.repositories.agregador;

import aplicacion.domain.hechosYSolicitudes.Hecho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface HechoRepository extends JpaRepository<Hecho, String> {

    @Query(value = "SELECT h FROM Hecho h WHERE h.fechaCarga > :fecha ORDER BY h.fechaCarga")
    Page<Hecho> findByFechaAfter(@Param("fecha")LocalDateTime fecha, Pageable pageable);
}
