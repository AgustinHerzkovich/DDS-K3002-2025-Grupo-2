package domain.repositorios;

import domain.colecciones.Coleccion;
import domain.colecciones.fuentes.Fuente;
import domain.colecciones.fuentes.FuenteId;
import org.springframework.data.jpa.repository.JpaRepository;
import domain.colecciones.fuentes.FuenteXColeccion;
import domain.colecciones.fuentes.FuenteXColeccionId;

import java.util.Optional;

public interface RepositorioDeFuentesXColeccion extends JpaRepository<FuenteXColeccion, FuenteXColeccionId > {
    Optional<FuenteXColeccion> findByFuente(Fuente fuente);

    Optional<FuenteXColeccion> findByFuenteIdAndColeccion(FuenteId fuenteId, Coleccion coleccion);
}