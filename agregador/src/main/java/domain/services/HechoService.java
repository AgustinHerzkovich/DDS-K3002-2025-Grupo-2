package domain.services;

import domain.colecciones.Coleccion;
import domain.colecciones.HechoXColeccion;
import domain.colecciones.fuentes.Fuente;
import domain.colecciones.fuentes.FuenteXColeccion;
import domain.colecciones.fuentes.HechoXFuente;
import domain.hechos.Hecho;
import domain.repositorios.RepositorioDeFuentesXColeccion;
import domain.repositorios.RepositorioDeHechos;
import domain.repositorios.RepositorioDeHechosXFuente;
import domain.repositorios.RepositorioDeHechosXColeccion;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HechoService {
    private final RepositorioDeHechos repositorioDeHechos;
    private final RepositorioDeHechosXFuente repositorioDeHechosXFuente;
    private final RepositorioDeHechosXColeccion repositorioDeHechosXColeccion;
    private final RepositorioDeFuentesXColeccion repositorioDeFuentesXColeccion;

    public HechoService(RepositorioDeHechos repositorioDeHechos, RepositorioDeHechosXFuente repositorioDeHechosXFuente, RepositorioDeHechosXColeccion repositorioDeHechosXColeccion, RepositorioDeFuentesXColeccion repositorioDeFuentesXColeccion) {
        this.repositorioDeHechos = repositorioDeHechos;
        this.repositorioDeHechosXFuente = repositorioDeHechosXFuente;
        this.repositorioDeHechosXColeccion = repositorioDeHechosXColeccion;
        this.repositorioDeFuentesXColeccion = repositorioDeFuentesXColeccion;
    }

    public void guardarHechos(List<Hecho> hechos) {
        repositorioDeHechos.saveAll(hechos);
    }

    public List<Hecho> obtenerHechos() {
        return repositorioDeHechos.findAll(); // TODO: Cambiar esto por traer los hechos de HechoXColeccion joineado con Hecho y que solo traiga los distinct
    }

    public void guardarHechosPorFuente(Map<Fuente, List<Hecho>> hechosPorFuente) {
        for (Map.Entry<Fuente, List<Hecho>> entry : hechosPorFuente.entrySet()) {
            Fuente fuente = entry.getKey();
            List<Hecho> hechos = entry.getValue();
            for (Hecho hecho : hechos) {
                HechoXFuente hechoPorFuente = new HechoXFuente(hecho, fuente);
                repositorioDeHechosXFuente.save(hechoPorFuente);
            }
        }
    }

    public void guardarHechosPorColeccion(Map<Fuente, List<Hecho>> hechosPorFuente) {
        for (Map.Entry<Fuente, List<Hecho>> entry : hechosPorFuente.entrySet()) {
            Fuente fuente = entry.getKey();
            List<Hecho> hechos = entry.getValue();

            // Buscar la colección asociada a la fuente
            Optional<FuenteXColeccion> fuenteXcoleccionOpt = repositorioDeFuentesXColeccion.findByFuente(fuente);

            if (fuenteXcoleccionOpt.isEmpty()) {
                System.out.println("No se encontró colección para la fuente: " + fuente.getId());
                continue;
            }

            Coleccion coleccion = fuenteXcoleccionOpt.get().getColeccion();

            // Filtrar los hechos que cumplen con todos los criterios de pertenencia
            List<HechoXColeccion> hechosFiltrados = hechos.stream()
                    .filter(coleccion::cumpleCriterios)
                    .map(hecho -> new HechoXColeccion(hecho, coleccion))
                    .toList();

            repositorioDeHechosXColeccion.saveAll(hechosFiltrados); // Solo guardar los hechos si cumplen con todos los criterios de pertenencia
        }
    }
    public Hecho obtenerHechoPorId(String idHecho) {
        return repositorioDeHechos.findByHechoId(idHecho);
    }

    public List<Hecho> obtenerHechosPorColeccion(String idColeccion) {
        return repositorioDeHechos.findByCollectionId(idColeccion);
    }

    public Map<Fuente, List<Hecho>> obtenerHechosPorColeccionPorFuente(String idColeccion) {
        // Obtiene todos los hechos hechos por fuente asociados a la colección
        List<HechoXFuente> hechosXFuente = repositorioDeHechosXFuente.findByCollectionId(idColeccion);

        // Transforma la lista de HechoXFuente a un map agrupado por Fuente
        return hechosXFuente.stream()
                .collect(Collectors.groupingBy(
                        HechoXFuente::getFuente,
                        Collectors.mapping(HechoXFuente::getHecho, Collectors.toList())
                ));
    }

    public List<Hecho> obtenerHechosCuradosPorColeccion(String idColeccion) {
        return repositorioDeHechos.findCuredByCollectionId(idColeccion);
    }

    public void guardarHecho(Hecho hecho) {
        repositorioDeHechos.save(hecho);
    }
}