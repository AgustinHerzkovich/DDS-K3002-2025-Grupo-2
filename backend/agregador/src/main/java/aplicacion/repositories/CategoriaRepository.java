package aplicacion.repositories;

import aplicacion.domain.hechos.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("""
        SELECT c
        FROM Categoria c
        WHERE c.nombre = :nombre
    """)
    Optional<Categoria> findByNombre(@Param("nombre") String nombre);
}
