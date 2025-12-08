package aplicacion.repositories.agregador;

import aplicacion.domain.hechosYSolicitudes.HechoXColeccion;
import aplicacion.domain.hechosYSolicitudes.HechoXColeccionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HechoXColeccionRepository extends JpaRepository<HechoXColeccion, HechoXColeccionId> {
}
