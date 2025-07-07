package domain.services;

import domain.hechos.Hecho;
import domain.colecciones.Coleccion;
import domain.repositorios.RepositorioDeColecciones;
import domain.repositorios.RepositorioDeHechos;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO: Analizar si tal vez conviene separar algunos metodos a un ColeccionService por ejemplo
@Service
public class HechoService {
    private final RepositorioDeColecciones repositorioDeColeccioness;
    private final RepositorioDeHechos repositorioDeHechos;

    public HechoService(RepositorioDeColecciones repositorioDeColeccioness, RepositorioDeHechos repositorioDeHechos) {
        this.repositorioDeColeccioness = repositorioDeColeccioness;
        this.repositorioDeHechos = repositorioDeHechos;
    }

    public void guardarColeccion(Coleccion coleccion) {
        repositorioDeColeccioness.save(coleccion);
    }

    public List<Coleccion> obtenerColecciones() {
        return repositorioDeColeccioness.findAll();
    }

    public List<Hecho> obtenerHechosIrrestrictosPorColeccion(Long idColeccion) {
        return repositorioDeHechos.findByCollectionId(idColeccion);
    }

    public List<Hecho> obtenerHechosCuradosPorColeccion(Long idColeccion) {
        return repositorioDeHechos.findCuredByCollectionId(idColeccion); // todo: esto deberia ir en repositorio de hechos x coleccion?
    }
}

//GIGA TODO: TENEMOS UN REPOSITORIO POR CADA TABLA DEL DER. EL PROBLEMA ESTA EN COMO HACES EL SELECT Y EL JOIN SIN TENER UNA BASE DE DATOS LEVANTADA
// TODO ENTONCES HAY QUE LEVANTAR LOS REPOS LOCALMENTE, QUE SE CONECTEN POR PK, FK Y QUE HAGAMOS EL SELECT A MANO DENTRO DE MEMORIA
// TODO LOS REPOSITORIOS DEBERIAN CONOCERSE ENTRE SI, O SEA QUE EL REPOSITORIO DE HECHOS CONOZCA AL REPOSITORIO DE COLECCIONES Y ASI PODER HACER EL JOIN