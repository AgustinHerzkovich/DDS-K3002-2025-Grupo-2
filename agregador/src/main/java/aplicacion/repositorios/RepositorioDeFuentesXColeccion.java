package aplicacion.repositorios;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.FuenteId;
import aplicacion.clasesIntermedias.FuenteXColeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import aplicacion.clasesIntermedias.FuenteXColeccionId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositorioDeFuentesXColeccion extends JpaRepository<FuenteXColeccion, FuenteXColeccionId > {
    Optional<FuenteXColeccion> findByFuente(Fuente fuente);

    Optional<FuenteXColeccion> findByFuenteIdAndColeccion(FuenteId fuenteId, Coleccion coleccion);
}