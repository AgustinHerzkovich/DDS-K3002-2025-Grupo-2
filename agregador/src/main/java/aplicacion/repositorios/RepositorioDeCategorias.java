package aplicacion.repositorios;

import aplicacion.domain.hechos.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositorioDeCategorias extends JpaRepository<Categoria, Long> {

    @Query("""
        SELECT c
        FROM Categoria c
        WHERE c.nombre = :nombre
    """)
    Optional<Categoria> findByNombre(String nombre);
}
