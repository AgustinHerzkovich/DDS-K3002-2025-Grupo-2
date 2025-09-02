package aplicacion.repositorios;

import aplicacion.domain.colecciones.fuentes.FuenteId;
import aplicacion.domain.colecciones.fuentes.HechoXFuente;
import aplicacion.domain.colecciones.fuentes.HechoXFuenteId;
import aplicacion.domain.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RepositorioDeHechosXFuente extends JpaRepository<HechoXFuente, HechoXFuenteId> {
    @Query("""
        SELECT hxf
        FROM HechoXFuente hxf
        JOIN Hecho h ON hxf.hecho.id = h.id
        JOIN HechoXColeccion hxc ON h.id = hxc.hecho.id
        WHERE hxc.coleccion.id = :idColeccion
    """)
    List<HechoXFuente> findByCollectionId(@Param("idColeccion") String idColeccion);

    @Query("""
        SELECT hxf.hecho AS hecho, COUNT(hxf.fuente) AS fuenteCount
        FROM HechoXFuente hxf
        JOIN HechoXColeccion hxc ON hxf.hecho.id = hxc.hecho.id
        WHERE hxc.coleccion.id = :idColeccion
        GROUP BY hxf.hecho
    """)
    List<Object[]> countHechosByFuente(@Param("idColeccion") String idColeccion);

    @Query("""
        SELECT CASE WHEN COUNT(hxf) > 0 THEN TRUE ELSE FALSE END
        FROM HechoXFuente hxf
        JOIN FuenteXColeccion fxc ON hxf.fuente.id = fxc.fuente.id
        WHERE hxf.fuente.id = :fuenteId AND fxc.coleccion.id = :coleccionId
    """)
    Boolean existsByFuenteIdAndColeccionId(FuenteId fuenteId, String coleccionId);
}