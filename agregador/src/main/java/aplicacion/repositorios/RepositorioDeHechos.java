package aplicacion.repositorios;

import aplicacion.domain.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioDeHechos extends JpaRepository<Hecho, String> {
    @Query("""
        SELECT h
        FROM Hecho h
        JOIN HechoXColeccion hc ON h.id = hc.hecho.id
        WHERE hc.coleccion.identificadorHandle = :idColeccion
    """)
    List<Hecho> findByCollectionId(@Param("idColeccion") String idColeccion);

    @Query("""
        SELECT h
        FROM Hecho h
        JOIN HechoXColeccion hc ON h.id = hc.hecho.id
        WHERE hc.coleccion.identificadorHandle = :idColeccion AND hc.consensuado = true
    """)
    List<Hecho> findCuredByCollectionId(@Param("idColeccion") String idColeccion);

    @Query("""
        SELECT h
        FROM Hecho h
        WHERE h.id = :idHecho
    """)
    Hecho findByHechoId(@Param("idHecho") String idHecho);
}