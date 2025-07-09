package domain.repositorios;

import domain.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioDeHechos extends JpaRepository<Hecho, Long> {
    // TODO: Revisar la query
    @Query("""
        SELECT h
        FROM Hecho h
        JOIN HechoXColeccion hc ON h = hc.hecho
        WHERE hc.coleccion = :idColeccion
    """)
    List<Hecho> findByCollectionId(@Param("idColeccion") String idColeccion);

    // TODO: Revisar la query
    @Query("""
        SELECT h
        FROM Hecho h
        JOIN HechoXColeccion hc ON h = hc.hecho
        WHERE hc.coleccion = :idColeccion AND hc.consensuado = true
    """)
    List<Hecho> findCuredByCollectionId(@Param("idColeccion") String idColeccion);
}