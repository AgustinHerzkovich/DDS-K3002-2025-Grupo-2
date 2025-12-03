package aplicacion.repositories;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio de Fuentes general
@Repository
public interface FuenteRepository extends JpaRepository<Fuente, String> {
    @Query("SELECT f.hechos FROM Fuente f WHERE f.id = :fuenteId")
    List<Hecho> findHechosByFuenteId(@Param("fuenteId") String fuenteId);

    @Query("SELECT f FROM Fuente f WHERE TYPE(f) = :tipoClass")
    Page<Fuente> findByTipo(@Param("tipoClass") Class<? extends Fuente> tipoClass, PageRequest pageable);

    @Query(value = "SELECT COUNT(DISTINCT fuente_id) FROM hecho_fuente ",
            countQuery = "SELECT COUNT(DISTINCT fuente_id) FROM hecho_fuente ",
            nativeQuery = true)
    Long countWithHechos();
}