package aplicacion.services;

import aplicacion.clasesIntermedias.HechoXColeccion;
import aplicacion.domain.algoritmos.*;
import aplicacion.domain.colecciones.Coleccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.input.ColeccionInputDto;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.input.ModificacionAlgoritmoInputDto;
import aplicacion.dto.mappers.*;
import aplicacion.dto.output.ColeccionOutputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.excepciones.ColeccionNoEncontradaException;
import aplicacion.excepciones.FuenteNoEncontradaException;
import aplicacion.repositorios.RepositorioDeColecciones;
import aplicacion.repositorios.RepositorioDeHechosXColeccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColeccionService {
    private final RepositorioDeColecciones repositorioDeColecciones;
    private final HechoService hechoService;
    private final RepositorioDeHechosXColeccion repositorioDeHechosXColeccion;
    private final ColeccionInputMapper coleccionInputMapper;
    private final ColeccionOutputMapper coleccionOutputMapper;
    private final HechoOutputMapper hechoOutputMapper;
    private final FuenteService fuenteService;
    private final FuenteInputMapper fuenteInputMapper;

    public ColeccionService(ColeccionInputMapper coleccionInputMapper, ColeccionOutputMapper coleccionOutputMapper, RepositorioDeColecciones repositorioDeColecciones, HechoService hechoService, RepositorioDeHechosXColeccion repositorioDeHechosXColeccion, HechoOutputMapper hechoOutputMapper, FuenteService fuenteService, FuenteInputMapper fuenteInputMapper) {
        this.repositorioDeColecciones = repositorioDeColecciones;
        this.hechoService = hechoService;
        this.repositorioDeHechosXColeccion = repositorioDeHechosXColeccion;
        this.coleccionInputMapper = coleccionInputMapper;
        this.coleccionOutputMapper = coleccionOutputMapper;
        this.hechoOutputMapper = hechoOutputMapper;
        this.fuenteService = fuenteService;
        this.fuenteInputMapper = fuenteInputMapper;
    }
    @Transactional
    public ColeccionOutputDto guardarColeccion(ColeccionInputDto coleccion) {
        List<Fuente> fuentes = coleccion.getFuentes().stream().map(fuente -> fuenteService.obtenerFuentePorId(fuente.getId())).toList(); // Verifica que existan o tira fuenteNotFound
        Coleccion coleccionLocal = coleccionInputMapper.map(coleccion);
        coleccionLocal.setFuentes(fuentes);
        Coleccion coleccionGuardada = repositorioDeColecciones.save(coleccionLocal);
        this.asociarHechosPreexistentes(coleccionGuardada);
        return coleccionOutputMapper.map(coleccionGuardada);
    }
    @Transactional
    public void asociarHechosPreexistentes(Coleccion coleccion) {
        System.out.println("Asociando hechos preexistentes de "+coleccion.getId() + " " + coleccion.getTitulo() + " con " + coleccion.getFuentes().size() + " fuentes");
        List<Fuente> fuentes = coleccion.getFuentes();
        for (Fuente fuente : fuentes) {
            asociarHechosPreexistentesDeFuenteAColeccion(coleccion, fuente);
        }
    }
    @Transactional
    public void asociarHechosPreexistentesDeFuenteAColeccion(Coleccion coleccion, Fuente fuente){
        System.out.println("Asociando " + coleccion.getId() + " " + coleccion.getTitulo() + " con " + fuente.getHechos().size() + " hechos");
        List<Hecho> hechosDeFuente = fuenteService.obtenerHechosPorFuente(fuente.getId());
        List<Hecho> hechosQueCumplenCriterios = hechosDeFuente.stream()
                .filter(coleccion::cumpleCriterios)
                .toList();
        List<HechoXColeccion> hechosXColeccion = hechosQueCumplenCriterios.stream()
                .map(hecho -> new HechoXColeccion(hecho, coleccion))
                .collect(Collectors.toList());
        repositorioDeHechosXColeccion.saveAll(hechosXColeccion);
    }

    public Page<ColeccionOutputDto> obtenerColeccionesDTO(Pageable pageable) { //ahora service devuelve todos en DTO. Se crean metodos nuevos de ser necesario.
        return repositorioDeColecciones.findAll(pageable).map(coleccionOutputMapper::map);
    }

    public Page<ColeccionOutputDto> obtenerColeccionesPorTextoLibre(String texto, Pageable pageable) {
        return repositorioDeColecciones.findByTextoLibre(texto, pageable).map(coleccionOutputMapper::map);
    }

    public List<Coleccion> obtenerColecciones() { //ahora service devuelve todos en DTO. Se crean metodos nuevos de ser necesario.
        return repositorioDeColecciones.findAll();
    }

    public Coleccion obtenerColeccion(String idColeccion) {
        return repositorioDeColecciones.findById(idColeccion).orElseThrow(() -> new IllegalArgumentException("Colección no encontrada con ID: " + idColeccion));
    }

    public ColeccionOutputDto obtenerColeccionDTO(String idColeccion) {
         return repositorioDeColecciones.findById(idColeccion)
                .map(coleccionOutputMapper::map)
                .orElseThrow(() -> new IllegalArgumentException("Colección no encontrada con ID: " + idColeccion));
    }

    public Page<HechoOutputDto> obtenerHechosIrrestrictosPorColeccion(String idColeccion,
                                                                      String categoria,
                                                                      LocalDateTime fechaReporteDesde,
                                                                      LocalDateTime fechaReporteHasta,
                                                                      LocalDateTime fechaAcontecimientoDesde,
                                                                      LocalDateTime fechaAcontecimientoHasta,
                                                                      Double latitud,
                                                                      Double longitud,
                                                                      String textoLibre,
                                                                      Pageable pageable) {

        Page<Hecho> hechosIrrestrictos;
        if (textoLibre != null && !textoLibre.isBlank()) {
            hechosIrrestrictos = hechoService.obtenerHechosPorColeccionYTextoLibre(idColeccion, categoria, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud, textoLibre, pageable);
        } else{
            hechosIrrestrictos = hechoService.obtenerHechosPorColeccion(idColeccion, categoria, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud, pageable);
        }

        return hechosIrrestrictos.map(hechoOutputMapper::map);
    }

    public Page<HechoOutputDto> obtenerHechosCuradosPorColeccionDTO(String idColeccion,
                                                                    String categoria,
                                                                    LocalDateTime fechaReporteDesde,
                                                                    LocalDateTime fechaReporteHasta,
                                                                    LocalDateTime fechaAcontecimientoDesde,
                                                                    LocalDateTime fechaAcontecimientoHasta,
                                                                    Double latitud,
                                                                    Double longitud,
                                                                    String textoLibre,
                                                                    Pageable pageable) {
        Page<Hecho> hechosCurados;
        if (textoLibre != null && !textoLibre.isBlank()) {
            hechosCurados = hechoService.obtenerHechosCuradosPorColeccionYTextoLibre(idColeccion, categoria, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud, textoLibre, pageable);
        } else{
            hechosCurados = hechoService.obtenerHechosCuradosPorColeccion(idColeccion, categoria, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud, pageable);;
        }

        return hechosCurados.map(hechoOutputMapper::map);
    }

    public void eliminarColeccion(String idColeccion) throws ColeccionNoEncontradaException{
        Coleccion coleccion = repositorioDeColecciones.findById(idColeccion)
                .orElseThrow(() -> new ColeccionNoEncontradaException("Colección no encontrada con ID: " + idColeccion));
        hechoService.borrarHechosPorColeccion(coleccion);
        repositorioDeColecciones.delete(coleccion);
        System.out.println("Colección eliminada: " + idColeccion);
    }

    public ColeccionOutputDto modificarAlgoritmoDeColeccion(String idColeccion, ModificacionAlgoritmoInputDto nuevoAlgoritmo) {
        Coleccion coleccion = repositorioDeColecciones.findById(idColeccion)
                .orElseThrow(() -> new IllegalArgumentException("Colección no encontrada con ID: " + idColeccion));
        coleccion.setTipoAlgoritmoConsenso(nuevoAlgoritmo.getAlgoritmoConsenso());
        Coleccion coleccionPersistida = repositorioDeColecciones.save(coleccion); // Updatea en la base de datos la colección con el nuevo algoritmo
        return coleccionOutputMapper.map(coleccionPersistida);
    }

    public ColeccionOutputDto agregarFuenteAColeccion(String coleccionId, FuenteInputDto fuenteInputDto) throws ColeccionNoEncontradaException {
        Fuente fuente = fuenteInputMapper.map(fuenteInputDto);
        fuente = fuenteService.guardarFuenteSiNoExiste(fuente);
        Coleccion coleccion = obtenerColeccion(coleccionId);
        coleccion.agregarFuente(fuente);
        coleccion = repositorioDeColecciones.save(coleccion);
        asociarHechosPreexistentesDeFuenteAColeccion(coleccion, fuente);        //Agrega hechos viejos a la coleccion, si es fuente nueva no se agrega nada
        return coleccionOutputMapper.map(coleccion);
    }

    @Transactional
    public ColeccionOutputDto quitarFuenteDeColeccion(String idColeccion, String fuenteId) throws FuenteNoEncontradaException {
        Coleccion coleccion = obtenerColeccion(idColeccion);
        Fuente fuente = fuenteService.obtenerFuentePorId(fuenteId);

        coleccion.quitarFuente(fuente);
        coleccion = repositorioDeColecciones.save(coleccion); // Updatea la colección después de quitar la fuente

        ColeccionOutputDto coleccionOutputDto = coleccionOutputMapper.map(coleccion);

        // Si en fuente por coleccion no quedan mas registros para esta fuente, entonces se eliminan las entradas de HechoXFuente que tengan este FuenteId
        if (!repositorioDeColecciones.existsByFuenteId(fuenteId)) {
            fuente.eliminarTodosLosHechos();
            fuenteService.guardarFuente(fuente);
        }

        // Quitamos de HechoXColeccion aquellos hechos que eran de esta fuente
        repositorioDeHechosXColeccion.deleteAllByFuenteId(fuenteId); // Eliminar hechos asociados a la fuente de la colección

        return coleccionOutputDto;
    }
}