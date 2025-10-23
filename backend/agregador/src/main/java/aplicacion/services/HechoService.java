package aplicacion.services;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.clasesIntermedias.HechoXColeccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.mappers.HechoInputMapper;
import aplicacion.dto.mappers.HechoOutputMapper;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.excepciones.ContribuyenteNoConfiguradoException;
import aplicacion.repositorios.RepositorioDeHechos;
import aplicacion.repositorios.RepositorioDeHechosXColeccion;
import aplicacion.excepciones.HechoNoEncontradoException;
import aplicacion.services.normalizador.NormalizadorDeHechos;
import aplicacion.utils.Md5Hasher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class HechoService {
    private final RepositorioDeHechos repositorioDeHechos;
    private final RepositorioDeHechosXColeccion repositorioDeHechosXColeccion;
    private final HechoOutputMapper hechoOutputMapper;
    private final NormalizadorDeHechos normalizadorDeHechos;
    private final HechoInputMapper hechoInputMapper;
    private final ContribuyenteService contribuyenteService;

    public HechoService(RepositorioDeHechos repositorioDeHechos, RepositorioDeHechosXColeccion repositorioDeHechosXColeccion, HechoOutputMapper hechoOutputMapper, NormalizadorDeHechos normalizadorDeHechos, HechoInputMapper hechoInputMapper, ContribuyenteService contribuyenteService) {
        this.repositorioDeHechos = repositorioDeHechos;
        this.repositorioDeHechosXColeccion = repositorioDeHechosXColeccion;
        this.hechoOutputMapper = hechoOutputMapper;
        this.normalizadorDeHechos = normalizadorDeHechos;
        this.hechoInputMapper = hechoInputMapper;
        this.contribuyenteService = contribuyenteService;
    }

    public void guardarHechos(List<Hecho> hechos) {
        for (Hecho hecho : hechos) {
            guardarOActualizarContribuyenteDeHecho(hecho);
        }
        repositorioDeHechos.saveAll(hechos);
    }

    public List<Hecho> obtenerHechos() {
        return repositorioDeHechos.findAll();
    }

    public List<HechoOutputDto> obtenerHechosPorTextoLibreDto(String categoria,
                                                              LocalDateTime fechaReporteDesde,
                                                              LocalDateTime fechaReporteHasta,
                                                              LocalDateTime fechaAcontecimientoDesde,
                                                              LocalDateTime fechaAcontecimientoHasta,
                                                              Double latitud,
                                                              Double longitud,
                                                              String textoLibre){
        return filtrarHechosQueryParam(obtenerHechosPorTextoLibre(textoLibre), categoria, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud);
    }

    public List<Hecho> obtenerHechosPorTextoLibre(String textoLibre) {
        return repositorioDeHechos.findByTextoLibre(textoLibre);
    }

    public List<HechoOutputDto> obtenerHechosAsDTO(String categoria,
                                                  LocalDateTime fechaReporteDesde,
                                                  LocalDateTime fechaReporteHasta,
                                                  LocalDateTime fechaAcontecimientoDesde,
                                                  LocalDateTime fechaAcontecimientoHasta,
                                                  Double latitud,
                                                  Double longitud) {
        return filtrarHechosQueryParam(obtenerHechos(), categoria, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud);
    }

    public void guardarHechoPorColeccion(HechoXColeccion hechoPorColeccion) {
        Hecho hecho = hechoPorColeccion.getHecho();
        Coleccion coleccion = hechoPorColeccion.getColeccion();
        // Si el hecho cumple los criterios de pertenencia de la colección, entonces se guarda, en caso contrario no
        if (coleccion.cumpleCriterios(hecho)) {
            repositorioDeHechosXColeccion.save(hechoPorColeccion);
        }
    }

    @Transactional
    public List<HechoOutputDto> obtenerHechosDeContribuyente( Long contribuyenteId ) throws ContribuyenteNoConfiguradoException {
        contribuyenteService.obtenerContribuyente(contribuyenteId);
        return repositorioDeHechos.findByAutorId(contribuyenteId).stream().map(hechoOutputMapper::map).toList();
    }

    public Hecho obtenerHechoPorId(String idHecho)  throws HechoNoEncontradoException{
        try{
            return repositorioDeHechos.findByHechoId(idHecho);
        }
        catch (Exception e){
            throw new HechoNoEncontradoException("No se encontro el hecho con id: " + idHecho);
        }
    }

    public HechoOutputDto obtenerHechoDto(String idHecho) throws HechoNoEncontradoException {
        Hecho hecho = obtenerHechoPorId(idHecho);
        return hechoOutputMapper.map(hecho);
    }

    public List<Hecho> obtenerHechosPorColeccion(String idColeccion) {
        return repositorioDeHechos.findByCollectionId(idColeccion);
    }

    public List<Hecho> obtenerHechosPorColeccionYTextoLibre(String idColeccion, String textoLibre) {
        return repositorioDeHechos.findByCollectionIdAndTextoLibre(idColeccion, textoLibre);
    }

    public List<Hecho> obtenerHechosCuradosPorColeccion(String idColeccion) {
        return repositorioDeHechos.findCuredByCollectionId(idColeccion);
    }

    public List<Hecho> obtenerHechosCuradosPorColeccionYTextoLibre(String idColeccion, String textoLibre) {
        return repositorioDeHechos.findCuredByCollectionIdAndTextoLibre(idColeccion, textoLibre);
    }

    public void guardarHecho(Hecho hecho) {
        guardarOActualizarContribuyenteDeHecho(hecho);
        repositorioDeHechos.save(hecho);
    }

    private void guardarOActualizarContribuyenteDeHecho(Hecho hecho) {
        if (hecho.getAutor() != null) {
            Contribuyente contribuyente = contribuyenteService.guardarOActualizar(hecho.getAutor());
            hecho.setAutor(contribuyente);
        }
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

    public void quitarHechosDeSublista(List<Hecho> listaOriginal, List<Hecho> hechosAQuitar){
        listaOriginal.removeIf(hechoA -> hechosAQuitar.stream().anyMatch(hechoB -> hechoA == hechoB));
    }

    public List<Hecho> hallarHechosDuplicadosDeBD(List<Hecho> hechosAEvaluar){
        Md5Hasher hasher = Md5Hasher.getInstance();
        List<String> codigosUnicos = hechosAEvaluar.stream().map(Hecho::getClaveUnica).map(hasher::hash).toList();
        return repositorioDeHechos.findByCodigoHasheadoIn(codigosUnicos);
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

    public List<Hecho> hallarHechosDuplicadosDeLista(List<Hecho> hechosAEvaluar){
        List<Hecho> hechosDuplicados = new ArrayList<>();
        Set<String> vistos = new HashSet<>();

        for (Hecho hecho : hechosAEvaluar) {
            if (!vistos.add(hecho.getClaveUnica())) {
                hechosDuplicados.add(hecho);
            }
        }
        return hechosDuplicados;
    }

    public List<HechoOutputDto> filtrarHechosQueryParam(List<Hecho> hechos,
                                                        String categoria,
                                                        LocalDateTime fechaReporteDesde,
                                                        LocalDateTime fechaReporteHasta,
                                                        LocalDateTime fechaAcontecimientoDesde,
                                                        LocalDateTime fechaAcontecimientoHasta,
                                                        Double latitud,
                                                        Double longitud) {
        return hechos.stream()
                .filter(h -> categoria == null || h.getCategoria().getNombre().equalsIgnoreCase(categoria))
                .filter(h -> fechaReporteDesde == null ||  h.getFechaCarga().isAfter(fechaReporteDesde))
                .filter(h -> fechaReporteHasta == null || h.getFechaCarga().isBefore(fechaReporteHasta))
                .filter(h -> fechaAcontecimientoDesde == null || h.getFechaAcontecimiento().isAfter(fechaAcontecimientoDesde))
                .filter(h -> fechaAcontecimientoHasta == null || h.getFechaAcontecimiento().isBefore(fechaAcontecimientoHasta))
                .filter(h -> latitud == null || h.getUbicacion().getLatitud().equals(latitud))
                .filter(h -> longitud == null || h.getUbicacion().getLongitud().equals(longitud))
                .map(hechoOutputMapper::map)
                .collect(Collectors.toList()); //convierte el stream de elementos (después de aplicar los .filter(...), .map(...), etc.) en una lista (List<T>) de resultados.
    }
}