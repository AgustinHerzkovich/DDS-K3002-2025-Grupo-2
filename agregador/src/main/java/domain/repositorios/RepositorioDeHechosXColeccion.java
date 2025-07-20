package domain.repositorios;

import domain.colecciones.HechoXColeccion;
import domain.colecciones.HechoXColeccionId;
import domain.colecciones.fuentes.FuenteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RepositorioDeHechosXColeccion extends JpaRepository<HechoXColeccion, HechoXColeccionId> {
    // TODO: usar estos metodos
    List<HechoXColeccion> findByColeccion_IdentificadorHandle(String idColeccion);

    @Transactional
    @Modifying
    @Query("""
    DELETE FROM HechoXColeccion hxc
    WHERE hxc.hecho IN (
        SELECT hxf.hecho FROM HechoXFuente hxf WHERE hxf.fuente.id = :fuenteId
    )
""")
    void deleteAllByFuenteId(@Param("fuenteId") FuenteId fuenteId);
    //List<HechoXColeccion> findByColeccionIdAndConsensuadoIsTrue(Long idColeccion);
}
