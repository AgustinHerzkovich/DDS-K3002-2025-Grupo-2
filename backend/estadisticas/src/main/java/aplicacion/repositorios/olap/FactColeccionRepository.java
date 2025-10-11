package aplicacion.repositorios.olap;

import aplicacion.domain.facts.FactColeccion;
import aplicacion.domain.id.FactColeccionId;
import aplicacion.dtos.ProvinciaConMasHechosDeColeccionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface FactColeccionRepository extends JpaRepository<FactColeccion, FactColeccionId> {
    @Query("""
    SELECT new aplicacion.dtos.ProvinciaConMasHechosDeColeccionDTO(
        c.dimensionUbicacion.id_ubicacion, c.dimensionUbicacion.provincia, c.dimensionUbicacion.pais, SUM(c.cantidadHechos), :coleccionId
    )
    FROM FactColeccion c
    WHERE c.dimensionColeccion.idColeccionAgregador = :coleccionId
    GROUP BY c.dimensionUbicacion
    ORDER BY SUM(c.cantidadHechos) DESC
    """)
    Page<ProvinciaConMasHechosDeColeccionDTO> provinciasOrdenadasPorCantidadHechos(@Param("coleccionId") String coleccionId, Pageable pageable);
}
