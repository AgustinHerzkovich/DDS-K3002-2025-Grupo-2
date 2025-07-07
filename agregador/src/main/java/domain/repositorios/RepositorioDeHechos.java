package domain.repositorios;

import domain.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RepositorioDeHechos extends JpaRepository<Hecho, Long>, RepositorioDeHechosCustom {

    public List<Hecho> findCuredByCollectionId(Long id); //TODO NECESITAMOS QUE SE CONECTE CON EL REPOSITORIO DE HECHOSXCOLECCION PARA TRAER SOLO AQUELLOS QUE ESTEN CURADOS
}