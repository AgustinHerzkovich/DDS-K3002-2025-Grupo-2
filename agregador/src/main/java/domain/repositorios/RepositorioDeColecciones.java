package domain.repositorios;

import domain.colecciones.Coleccion;
import domain.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RepositorioDeColecciones extends JpaRepository<Coleccion, String> {

}
