package domain.services;

import domain.colecciones.fuentes.Fuente;
import domain.colecciones.fuentes.HechoXFuente;
import domain.hechos.Hecho;
import domain.repositorios.RepositorioDeHechos;
import domain.repositorios.RepositorioDeHechosXFuente;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HechoService {
    private final RepositorioDeHechos repositorioDeHechos;
    private final RepositorioDeHechosXFuente repositorioDeHechosXFuente;

    public HechoService(RepositorioDeHechos repositorioDeHechos, RepositorioDeHechosXFuente repositorioDeHechosXFuente) {
        this.repositorioDeHechos = repositorioDeHechos;
        this.repositorioDeHechosXFuente = repositorioDeHechosXFuente;
    }

    public void guardarHechos(List<Hecho> hechos) {
        repositorioDeHechos.saveAll(hechos);
    }

    public void guardarHechosPorFuente(Map<Fuente, List<Hecho>> hechosPorFuente) {
        for (Map.Entry<Fuente, List<Hecho>> entry : hechosPorFuente.entrySet()) {
            Fuente fuente = entry.getKey();
            List<Hecho> hechos = entry.getValue();
            for (Hecho hecho : hechos) {
                HechoXFuente hechoPorFuente = new HechoXFuente(hecho, fuente);
                repositorioDeHechosXFuente.save(hechoPorFuente);
            }
            this.guardarHechos(hechos); // Guarda los hechos asociados a la fuente
        }
    }

    public List<Hecho> obtenerHechosPorColeccion(String idColeccion) {
        return repositorioDeHechos.findByCollectionId(idColeccion);
    }

    public Map<Fuente, List<Hecho>> obtenerHechosPorColeccionPorFuente(String idColeccion) {
        // Obtiene todos los hechos hechos por fuente asociados a la colección
        List<HechoXFuente> hechosXFuente = repositorioDeHechosXFuente.findByCollectionId(idColeccion);

        // Transforma la lista de HechoXFuente a un mapa agrupado por Fuente
        return hechosXFuente.stream()
                .collect(Collectors.groupingBy(
                        HechoXFuente::getFuente,
                        Collectors.mapping(HechoXFuente::getHecho, Collectors.toList())
                ));
    }

    public List<Hecho> obtenerHechosCuradosPorColeccion(String idColeccion) {
        return repositorioDeHechos.findCuredByCollectionId(idColeccion);
    }

}