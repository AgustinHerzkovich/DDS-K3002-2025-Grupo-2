package aplicacion.repositorios;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio de Fuentes general
@Repository
public interface RepositorioDeFuentes extends JpaRepository<Fuente, String> {
    @Query("SELECT f.hechos FROM Fuente f WHERE f.id = :fuenteId")
    List<Hecho> findHechosByFuenteId(@Param("fuenteId") String fuenteId);
}