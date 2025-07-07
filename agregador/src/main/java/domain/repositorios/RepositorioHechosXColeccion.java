package domain.repositorios;

import domain.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioHechosXColeccion extends JpaRepository<Hecho, Long> {
    //TODO:
    public default void agregarTodos(List<Hecho> todosLosHechos, String idColeccion) {

    }
    // Todos los hechos de una colección
    List<Hecho> findByColeccionId(Long idColeccion);

    // Hechos consensuados de una colección
    List<Hecho> findByColeccionIdAndConsensuadoTrue(Long idColeccion);

}
