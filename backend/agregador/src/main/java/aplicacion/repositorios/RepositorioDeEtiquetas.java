package aplicacion.repositorios;

import aplicacion.domain.hechos.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositorioDeEtiquetas extends JpaRepository<Etiqueta, Long> {
    @Query("SELECT e FROM Etiqueta e WHERE e.nombre = :nombre")
    Optional<Etiqueta> findByNombre(@Param("nombre") String nombre);
}
