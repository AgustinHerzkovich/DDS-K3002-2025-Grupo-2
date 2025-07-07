package domain.services;

import domain.colecciones.fuentes.Fuente;
import domain.hechos.Hecho;
import domain.repositorios.RepositorioDeHechos;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

// TODO: Analizar si tal vez conviene separar algunos metodos a un ColeccionService por ejemplo
@Service
public class HechoService {
    private final RepositorioDeHechos repositorioDeHechos;

    public HechoService(RepositorioDeHechos repositorioDeHechos) {
        this.repositorioDeHechos = repositorioDeHechos;
    }

    public void guardarHechos(List<Map.Entry<List<Hecho>, Fuente>> hechos) {
        repositorioDeHechos.saveAll(hechos); // TODO: Esto debe recibir una lista de tuplas Map y ver que hacer con la fuente
        //todo hay que crear una tabla hechosXFuente que relacione los hechos con las fuentes de donde provienen
        // implica hacer 2 clases mas y la puta madre que lo pario todo, pero bueno, es lo que hay
    }

    public List<Hecho> obtenerHechosPorColeccion(String idColeccion) {
        return repositorioDeHechos.findByCollectionId(idColeccion);
    }

    public List<Hecho> obtenerHechosCuradosPorColeccion(String idColeccion) {
        return repositorioDeHechos.findCuredByCollectionId(idColeccion);
    }

}

//GIGA TODO: TENEMOS UN REPOSITORIO POR CADA TABLA DEL DER. EL PROBLEMA ESTA EN COMO HACES EL SELECT Y EL JOIN SIN TENER UNA BASE DE DATOS LEVANTADA
// TODO ENTONCES HAY QUE LEVANTAR LOS REPOS LOCALMENTE, QUE SE CONECTEN POR PK, FK Y QUE HAGAMOS EL SELECT A MANO DENTRO DE MEMORIA
// TODO LOS REPOSITORIOS DEBERIAN CONOCERSE ENTRE SI, O SEA QUE EL REPOSITORIO DE HECHOS CONOZCA AL REPOSITORIO DE COLECCIONES Y ASI PODER HACER EL JOIN