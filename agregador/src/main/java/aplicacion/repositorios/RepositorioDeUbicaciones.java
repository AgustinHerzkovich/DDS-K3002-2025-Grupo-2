package aplicacion.repositorios;

import aplicacion.domain.hechos.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositorioDeUbicaciones extends JpaRepository<Ubicacion, Long> {
    @Query("SELECT u FROM Ubicacion u WHERE u.latitud = :latitud AND u.longitud = :longitud")
    Optional<Ubicacion> findByLatitudAndLongitud(Double latitud, Double longitud);
}
