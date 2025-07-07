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
    private final RepositorioDeColecciones repositorio_de_colecciones;
    private final RepositorioDeHechos repositorio_de_hechos;

    public HechoService(RepositorioDeColecciones repositorio_de_colecciones, RepositorioDeHechos repositorio_de_hechos) {
        this.repositorio_de_colecciones = repositorio_de_colecciones;
        this.repositorio_de_hechos = repositorio_de_hechos;
    }

    public void guardarColeccion(Coleccion coleccion) {
        repositorio_de_colecciones.save(coleccion);
    }

    public List<Coleccion> obtenerColecciones() {
        return repositorio_de_colecciones.findAll();
    }

    public List<Hecho> obtenerHechosIrrestrictosPorColeccion(String id_coleccion) {
        return repositorio_de_hechos.findByColeccionId(id_coleccion);
    }

    public List<Hecho> obtenerHechosCuradosPorColeccion(String id_coleccion) {
        return repositorio_de_hechos.findCuredByColeccionId(id_coleccion);
    }
}

//GIGA TODO: TENEMOS UN REPOSITORIO POR CADA TABLA DEL DER. EL PROBLEMA ESTA EN COMO HACES EL SELECT Y EL JOIN SIN TENER UNA BASE DE DATOS LEVANTADA
// TODO ENTONCES HAY QUE LEVANTAR LOS REPOS LOCALMENTE, QUE SE CONECTEN POR PK, FK Y QUE HAGAMOS EL SELECT A MANO DENTRO DE MEMORIA
// TODO LOS REPOSITORIOS DEBERIAN CONOCERSE ENTRE SI, O SEA QUE EL REPOSITORIO DE HECHOS CONOZCA AL REPOSITORIO DE COLECCIONES Y ASI PODER HACER EL JOIN