package aplicacion.services;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.clasesIntermedias.HechoXColeccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.mappers.HechoInputMapper;
import aplicacion.dto.mappers.HechoOutputMapper;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.repositorios.RepositorioDeHechos;
import aplicacion.repositorios.RepositorioDeHechosXColeccion;
import aplicacion.excepciones.HechoNoEncontradoException;
import aplicacion.services.normalizador.NormalizadorDeHechos;
import aplicacion.utils.Md5Hasher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HechoService {
    private final RepositorioDeHechos repositorioDeHechos;
    private final RepositorioDeHechosXColeccion repositorioDeHechosXColeccion;
    private final HechoOutputMapper hechoOutputMapper;
    private final NormalizadorDeHechos normalizadorDeHechos;
    private final HechoInputMapper hechoInputMapper;

    public HechoService(RepositorioDeHechos repositorioDeHechos, RepositorioDeHechosXColeccion repositorioDeHechosXColeccion, HechoOutputMapper hechoOutputMapper, NormalizadorDeHechos normalizadorDeHechos, HechoInputMapper hechoInputMapper) {
        this.repositorioDeHechos = repositorioDeHechos;
        this.repositorioDeHechosXColeccion = repositorioDeHechosXColeccion;
        this.hechoOutputMapper = hechoOutputMapper;
        this.normalizadorDeHechos = normalizadorDeHechos;
        this.hechoInputMapper = hechoInputMapper;
    }

    public void guardarHechos(List<Hecho> hechos) {
        repositorioDeHechos.saveAll(hechos);
    }

    public List<Hecho> obtenerHechos() {
        return repositorioDeHechos.findAll(); // TODO: Cambiar esto por traer los hechos de HechoXColeccion joineado con Hecho y que solo traiga los distinct
    }
    public List<HechoOutputDto> obtenerHechosAsDTO() {
        return obtenerHechos().stream().map(hechoOutputMapper::map).toList();
    }


    public void guardarHechoPorColeccion(HechoXColeccion hechoPorColeccion) {
        Hecho hecho = hechoPorColeccion.getHecho();
        Coleccion coleccion = hechoPorColeccion.getColeccion();
        // Si el hecho cumple los criterios de pertenencia de la colección, entonces se guarda, en caso contrario no
        if (coleccion.cumpleCriterios(hecho)) {
            repositorioDeHechosXColeccion.save(hechoPorColeccion);
        }
    }
    public Hecho obtenerHechoPorId(String idHecho)  throws HechoNoEncontradoException{
        try{
            return repositorioDeHechos.findByHechoId(idHecho);
        }
        catch (Exception e){
            throw new HechoNoEncontradoException("No se encontro el hecho con id: " + idHecho);
        }
    }

    public List<Hecho> obtenerHechosPorColeccion(String idColeccion) {
        return repositorioDeHechos.findByCollectionId(idColeccion);
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


    public HechoOutputDto agregarHecho(HechoInputDto hechoInputDTO) {
        Hecho hecho = hechoInputMapper.map(hechoInputDTO);
        normalizadorDeHechos.normalizar(hecho);
        hecho = repositorioDeHechos.save(hecho);
        return hechoOutputMapper.map(hecho);
    }

    public void borrarHechosPorColeccion(Coleccion coleccion) {
        repositorioDeHechosXColeccion.deleteAllByColeccionId(coleccion.getId());
    }

    public List<Hecho> hallarHechosDuplicadosDeLista(List<Hecho> hechosAEvaluar){
        List<Hecho> hechosDuplicados = new ArrayList<>();
        Set<Hecho> vistos = new HashSet<>();

        for (Hecho hecho : hechosAEvaluar) {
            if (!vistos.add(hecho)) {
                hechosDuplicados.add(hecho);
            }
        }
        return hechosDuplicados;
    }

    public void quitarHechosSegunCodigoUnico(List<Hecho> listaOriginal, List<Hecho> hechosAQuitar){
        Md5Hasher hasher = Md5Hasher.getInstance();
        List<String> hechosAQuitarHashcode =hechosAQuitar.stream().map(Hecho::getClaveUnica).map(c -> hasher.hash(c)).toList();
        hechosAQuitar.removeIf(he -> hechosAQuitarHashcode.contains(hasher.hash(he.getClaveUnica())));
    }

    public List<Hecho> hallarHechosDuplicadosDeBD(List<Hecho> hechosAEvaluar){
        Md5Hasher hasher = Md5Hasher.getInstance();
        List<String> codigosUnicos = hechosAEvaluar.stream().map(Hecho::getClaveUnica).map(c -> hasher.hash(c)).toList();
        List<Hecho> hechosDuplicados = repositorioDeHechos.findByCodigoHasheadoIn(codigosUnicos);
        return hechosDuplicados;
    }

    public Map<Hecho, Long> contarHechosPorFuente(Coleccion coleccion) {
        List<Fuente> fuentes = coleccion.getFuentes();
        List<Hecho> hechos = fuentes.stream().flatMap(fuente -> fuente.getHechos().stream()).toList();
        Set<Hecho> hechosUnicos = new HashSet<>(hechos);
        Map<Hecho, Long> returnMap = new HashMap<>();
        for(Hecho hechoUnico : hechosUnicos){
            Long ocurrencias = 0L;
            for(Fuente fuente : fuentes){
                if(fuente.getHechos().contains(hechoUnico))
                    ocurrencias++;
            }
            returnMap.put(hechoUnico, ocurrencias);
        }
        return returnMap;
    }
}