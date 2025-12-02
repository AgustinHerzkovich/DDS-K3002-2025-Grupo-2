package aplicacion.repositories.olap;

import aplicacion.domain.dimensiones.DimensionCategoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface DimensionCategoriaRepository extends JpaRepository<DimensionCategoria, String> {
    @Query(
            value = """
                    SELECT * FROM DimensionCategoria dc
                    WHERE dc.nombre IN (:categorias)""",
            nativeQuery = true
    )
    List<DimensionCategoria> findByNombreCategoria(@Param("categorias") Set<String> categorias);

    @Query("SELECT c FROM DimensionCategoria c " +
            "WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<DimensionCategoria> findSearch(@Param("search") String search, Pageable pageable);

}
