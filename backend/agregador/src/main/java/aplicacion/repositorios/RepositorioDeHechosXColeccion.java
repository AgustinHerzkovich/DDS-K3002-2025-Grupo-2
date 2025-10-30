package aplicacion.repositorios;

import aplicacion.clasesIntermedias.HechoXColeccion;
import aplicacion.clasesIntermedias.HechoXColeccionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RepositorioDeHechosXColeccion extends JpaRepository<HechoXColeccion, HechoXColeccionId> {
    @Modifying
    @Transactional
    @Query(value = """
    DELETE FROM hecho_coleccion 
    WHERE hecho_id IN (
        SELECT hecho_id FROM hecho_fuente WHERE fuente_id = :fuenteId
    )
""", nativeQuery = true)
    void deleteAllByFuenteId(@Param("fuenteId") String fuenteId);
    @Transactional
    @Modifying
    @Query("""
    DELETE FROM HechoXColeccion hxc
    WHERE hxc.coleccion.id = :coleccionID
    """)
    void deleteAllByColeccionId(@Param("coleccionID") String coleccionID);
}
