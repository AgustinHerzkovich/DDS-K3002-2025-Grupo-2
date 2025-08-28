package aplicacion.repositorios;

import aplicacion.domain.colecciones.fuentes.HechoXFuente;
import aplicacion.domain.colecciones.fuentes.HechoXFuenteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioDeHechosXFuente extends JpaRepository<HechoXFuente, HechoXFuenteId> {
    @Query("""
        SELECT hxf
        FROM HechoXFuente hxf
        JOIN Hecho h ON hxf.hecho.id = h.id
        JOIN HechoXColeccion hxc ON h.id = hxc.hecho.id
        WHERE hxc.coleccion.identificadorHandle = :idColeccion
    """)
    List<HechoXFuente> findByCollectionId(@Param("idColeccion") String idColeccion);
}