package domain.repositorios;

import domain.colecciones.HechoXColeccion;
import domain.colecciones.HechoXColeccionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RepositorioDeHechosXColeccion extends JpaRepository<HechoXColeccion, HechoXColeccionId> {
    // TODO: usar estos metodos
    List<HechoXColeccion> findByCollectionId(Long idColeccion);
    List<HechoXColeccion> findByCollectionIdAndConsensuadoIsTrue(Long idColeccion);

    @Modifying
    @Transactional
    @Query("UPDATE HechoXColeccion h SET h.consensuado = :consensuado WHERE h.id = :uuid")
    void update(Long uuid, Boolean consensuado);
}
