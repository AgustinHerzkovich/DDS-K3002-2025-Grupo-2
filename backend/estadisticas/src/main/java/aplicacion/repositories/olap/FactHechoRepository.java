package aplicacion.repositories.olap;

import aplicacion.domain.facts.FactHecho;
import aplicacion.domain.id.FactHechoId;
import aplicacion.dtos.CategoriaConMasHechosDTO;
import aplicacion.dtos.HoraConMasHechosDeCategoriaDTO;
import aplicacion.dtos.ProvinciaConMasHechosDeCategoriaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface FactHechoRepository extends JpaRepository<FactHecho, FactHechoId> {
    @Query(value = """
                SELECT new aplicacion.dtos.CategoriaConMasHechosDTO(
                    h.dimensionCategoria.idCategoria, h.dimensionCategoria.nombre, COUNT(h)
                )
                FROM FactHecho h
                GROUP BY h.dimensionCategoria
                ORDER BY COUNT(h) DESC
                """)
    Page<CategoriaConMasHechosDTO> categoriaConMasHechos(Pageable pageable);

    @Query(value = """
                SELECT new aplicacion.dtos.ProvinciaConMasHechosDeCategoriaDTO(
                    h.dimensionUbicacion.id_ubicacion, h.dimensionUbicacion.provincia, h.dimensionUbicacion.pais, SUM(h.cantidadDeHechos)
                )
                FROM FactHecho h
                GROUP BY h.dimensionUbicacion
                ORDER BY SUM(h.cantidadDeHechos) DESC
                """)
    Page<ProvinciaConMasHechosDeCategoriaDTO> provinciaConMasHechos(Pageable pageable);

    @Query(value = """
                SELECT new aplicacion.dtos.ProvinciaConMasHechosDeCategoriaDTO(
                    h.dimensionUbicacion.id_ubicacion, h.dimensionUbicacion.provincia, h.dimensionUbicacion.pais, SUM(h.cantidadDeHechos), :categoria
                )
                FROM FactHecho h
                WHERE h.dimensionCategoria.nombre = :categoria
                GROUP BY h.dimensionUbicacion
                ORDER BY SUM(h.cantidadDeHechos) DESC
                """)
    Page<ProvinciaConMasHechosDeCategoriaDTO> provinciaConMasHechosDeCategoria(@Param("categoria") String categoria, Pageable pageable);

    @Query(
            value = """
            SELECT new aplicacion.dtos.HoraConMasHechosDeCategoriaDTO(
                h.dimensionTiempo.hora, SUM(h.cantidadDeHechos), :nombreCategoria
            )
            FROM FactHecho h
            WHERE h.dimensionCategoria.nombre = :nombreCategoria
            GROUP BY h.dimensionTiempo.hora
            ORDER BY SUM(h.cantidadDeHechos) DESC
            """)
    Page<HoraConMasHechosDeCategoriaDTO> obtenerHoraConMasHechosDeCategoria(@Param("nombreCategoria") String nombreCategoria, Pageable pageable);
}