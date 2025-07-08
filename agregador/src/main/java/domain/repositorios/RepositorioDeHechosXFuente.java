package domain.repositorios;

import domain.colecciones.fuentes.HechoXFuente;
import domain.colecciones.fuentes.HechoXFuenteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositorioDeHechosXFuente extends JpaRepository<HechoXFuente, HechoXFuenteId> {
    @Query("""
        SELECT hxf
        FROM HechoXFuente hxf
        JOIN Hecho h ON hxf.hecho = h
        JOIN HechoXColeccion hxc ON h = hxc.hecho
        WHERE hxc.coleccion = :idColeccion
    """)
    List<HechoXFuente> findByCollectionId(String idColeccion);
}