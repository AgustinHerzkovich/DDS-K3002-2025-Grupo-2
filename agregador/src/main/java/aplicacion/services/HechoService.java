package aplicacion.services;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.domain.colecciones.HechoXColeccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.HechoXFuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.repositorios.RepositorioDeFuentesXColeccion;
import aplicacion.repositorios.RepositorioDeHechos;
import aplicacion.repositorios.RepositorioDeHechosXFuente;
import aplicacion.repositorios.RepositorioDeHechosXColeccion;
import aplicacion.excepciones.HechoNoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    public void guardarHechoPorFuente(HechoXFuente hechoPorFuente) {
        repositorioDeHechosXFuente.save(hechoPorFuente);
    }

    public void guardarHechoPorColeccion(HechoXColeccion hechoPorColeccion) {
        Hecho hecho = hechoPorColeccion.getHecho();
        Coleccion coleccion = hechoPorColeccion.getColeccion();
        // Si el hecho cumple los criterios de pertenencia de la colección, entonces se guarda, en caso contrario no
        if (coleccion.cumpleCriterios(hecho)) {
            repositorioDeHechosXColeccion.save(hechoPorColeccion);
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

    public Hecho obtenerDuplicado(Hecho hecho) throws HechoNoEncontradoException {
        return repositorioDeHechos.findDuplicado(
                hecho.getTitulo(),
                hecho.getDescripcion(),
                hecho.getCategoria(),
                hecho.getUbicacion(),
                hecho.getFechaAcontecimiento(),
                hecho.getContenidoTexto()
        ).orElseThrow(() -> new HechoNoEncontradoException("No se encontró un hecho duplicado."));
    }

    public Map<Hecho, Integer> contarHechosPorFuente(Coleccion coleccion) {
        return repositorioDeHechosXFuente.countHechosByFuente(coleccion.getId());
    }
}