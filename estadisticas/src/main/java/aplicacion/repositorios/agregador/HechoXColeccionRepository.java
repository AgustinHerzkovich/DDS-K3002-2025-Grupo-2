package aplicacion.repositorios.agregador;

import aplicacion.domain.hechosYSolicitudes.Hecho;
import aplicacion.domain.hechosYSolicitudes.HechoXColeccion;
import aplicacion.domain.hechosYSolicitudes.HechoXColeccionId;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface HechoXColeccionRepository extends JpaRepository<HechoXColeccion, HechoXColeccionId> {
}
