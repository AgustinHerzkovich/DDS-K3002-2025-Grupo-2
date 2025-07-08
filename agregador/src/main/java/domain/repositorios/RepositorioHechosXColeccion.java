package domain.repositorios;

import domain.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Modifying
    @Transactional
    @Query("UPDATE HechoXColeccion h SET h.consensuado = :consensuado WHERE h.id = :uuid")
    void update(Long uuid, Boolean consensuado);

    @Transactional
    default void updateAll(List<Hecho> hechos, Boolean consensuado) {
        for (Hecho hecho : hechos) {
            update(hecho.getId(), consensuado);
        }
    }
}
