package aplicacion.repositorios.olap;

import aplicacion.domain.dimensiones.DimensionCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DimensionCategoriaRepository extends JpaRepository<DimensionCategoria, String> {
    @Query(
            value = """
                    SELECT * FROM DimensionCategoria dc +
                    WHERE dc.nombre IN (:categorias)""",
            nativeQuery = true
    )
    List<DimensionCategoria> findByNombreCategoria(List<String> categorias);
}
