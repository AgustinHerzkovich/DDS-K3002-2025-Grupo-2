package aplicacion.repositorios;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.clasesIntermedias.FuenteXColeccion;
import aplicacion.domain.colecciones.fuentes.FuenteId;
import org.springframework.data.jpa.repository.JpaRepository;
import aplicacion.clasesIntermedias.FuenteXColeccionId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RepositorioDeFuentesXColeccion extends JpaRepository<FuenteXColeccion, FuenteXColeccionId > {
    Optional<FuenteXColeccion> findByFuente(Fuente fuente);

    Optional<FuenteXColeccion> findByFuenteIdAndColeccion(FuenteId fuenteId, Coleccion coleccion);

    @Transactional
    @Modifying
    @Query("""
    DELETE FROM FuenteXColeccion fxc
    WHERE fxc.coleccion.id = :coleccionID
    """)
    void deleteAllByColeccionId(@Param("coleccionID") String coleccionID);

    boolean existsByFuenteId(FuenteId fuenteId);
}