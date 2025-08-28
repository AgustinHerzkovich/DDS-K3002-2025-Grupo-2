package aplicacion.repositorios;

import aplicacion.domain.colecciones.HechoXColeccion;
import aplicacion.domain.colecciones.HechoXColeccionId;
import aplicacion.domain.colecciones.fuentes.FuenteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RepositorioDeHechosXColeccion extends JpaRepository<HechoXColeccion, HechoXColeccionId> {
    @Transactional
    @Modifying
    @Query("""
    DELETE FROM HechoXColeccion hxc
    WHERE hxc.hecho IN (
        SELECT hxf.hecho FROM HechoXFuente hxf WHERE hxf.fuente.id = :fuenteId
    )
""")
    void deleteAllByFuenteId(@Param("fuenteId") FuenteId fuenteId);
}
