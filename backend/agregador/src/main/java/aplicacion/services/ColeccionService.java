package aplicacion.services;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.input.ColeccionInputDto;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.input.ModificacionAlgoritmoInputDto;
import aplicacion.dto.mappers.*;
import aplicacion.dto.output.ColeccionOutputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.events.ColeccionCreadaEvent;
import aplicacion.events.FuenteAgregadaAColeccionEvent;
import aplicacion.excepciones.ColeccionNoEncontradaException;
import aplicacion.excepciones.FuenteNoEncontradaException;
import aplicacion.repositories.ColeccionRepository;
import aplicacion.repositories.HechoXColeccionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ColeccionService {
    private final ColeccionRepository coleccionRepository;
    private final HechoService hechoService;
    private final HechoXColeccionRepository hechoXColeccionRepository;
    private final ColeccionInputMapper coleccionInputMapper;
    private final ColeccionOutputMapper coleccionOutputMapper;
    private final HechoOutputMapper hechoOutputMapper;
    private final FuenteService fuenteService;
    private final FuenteInputMapper fuenteInputMapper;
    private final ApplicationEventPublisher eventPublisher;

    public ColeccionService(ColeccionInputMapper coleccionInputMapper, ColeccionOutputMapper coleccionOutputMapper, ColeccionRepository coleccionRepository, HechoService hechoService, HechoXColeccionRepository hechoXColeccionRepository, HechoOutputMapper hechoOutputMapper, FuenteService fuenteService, FuenteInputMapper fuenteInputMapper, ApplicationEventPublisher eventPublisher) {
        this.coleccionRepository = coleccionRepository;
        this.hechoService = hechoService;
        this.hechoXColeccionRepository = hechoXColeccionRepository;
        this.coleccionInputMapper = coleccionInputMapper;
        this.coleccionOutputMapper = coleccionOutputMapper;
        this.hechoOutputMapper = hechoOutputMapper;
        this.fuenteService = fuenteService;
        this.fuenteInputMapper = fuenteInputMapper;
        this.eventPublisher = eventPublisher;
    }
    @Transactional
    public ColeccionOutputDto guardarColeccion(ColeccionInputDto coleccion) {
        List<Fuente> fuentes = coleccion.getFuentes().stream().map(fuente -> fuenteService.obtenerFuentePorId(fuente.getId())).toList(); // Verifica que existan o tira fuenteNotFound
        Coleccion coleccionLocal = coleccionInputMapper.map(coleccion);
        coleccionLocal.setFuentes(fuentes);
        Coleccion coleccionGuardada = coleccionRepository.save(coleccionLocal);

        // Publicar evento que se ejecutará después del commit de la transacción
        eventPublisher.publishEvent(new ColeccionCreadaEvent(this, coleccionGuardada));

        return coleccionOutputMapper.map(coleccionGuardada);
    }

    public Page<ColeccionOutputDto> obtenerColeccionesDTO(Pageable pageable) { //ahora service devuelve todos en DTO. Se crean metodos nuevos de ser necesario.
        return coleccionRepository.findAll(pageable).map(coleccionOutputMapper::map);
    }

    public Page<ColeccionOutputDto> obtenerColeccionesPorTextoLibre(String texto, Pageable pageable) {
        return coleccionRepository.findByTextoLibre(texto, pageable).map(coleccionOutputMapper::map);
    }

    public List<Coleccion> obtenerColecciones() { //ahora service devuelve todos en DTO. Se crean metodos nuevos de ser necesario.
        return coleccionRepository.findAll();
    }

    public Coleccion obtenerColeccion(String idColeccion) {
        return coleccionRepository.findById(idColeccion).orElseThrow(() -> new IllegalArgumentException("Colección no encontrada con ID: " + idColeccion));
    }

    public ColeccionOutputDto obtenerColeccionDTO(String idColeccion) {
         return coleccionRepository.findById(idColeccion)
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
                                                                      Double radio,
                                                                      String textoLibre,
                                                                      Pageable pageable) {

        return hechoService.obtenerHechosIrrestrictosPorColeccion(idColeccion, categoria, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud, radio, textoLibre, pageable);
    }

    public Page<HechoOutputDto> obtenerHechosCuradosPorColeccion(String idColeccion,
                                                                 String categoria,
                                                                 LocalDateTime fechaReporteDesde,
                                                                 LocalDateTime fechaReporteHasta,
                                                                 LocalDateTime fechaAcontecimientoDesde,
                                                                 LocalDateTime fechaAcontecimientoHasta,
                                                                 Double latitud,
                                                                 Double longitud,
                                                                 Double radio,
                                                                 String textoLibre,
                                                                 Pageable pageable) {

        return hechoService.obtenerHechosCuradosPorColeccion(idColeccion, categoria, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud, radio, textoLibre, pageable);
    }

    public void eliminarColeccion(String idColeccion) throws ColeccionNoEncontradaException{
        Coleccion coleccion = coleccionRepository.findById(idColeccion)
                .orElseThrow(() -> new ColeccionNoEncontradaException("Colección no encontrada con ID: " + idColeccion));
        hechoService.borrarHechosPorColeccion(coleccion);
        coleccionRepository.delete(coleccion);
        System.out.println("Colección eliminada: " + idColeccion);
    }

    public ColeccionOutputDto modificarAlgoritmoDeColeccion(String idColeccion, ModificacionAlgoritmoInputDto nuevoAlgoritmo) {
        Coleccion coleccion = coleccionRepository.findById(idColeccion)
                .orElseThrow(() -> new IllegalArgumentException("Colección no encontrada con ID: " + idColeccion));
        coleccion.setTipoAlgoritmoConsenso(nuevoAlgoritmo.getAlgoritmoConsenso());
        Coleccion coleccionPersistida = coleccionRepository.save(coleccion); // Updatea en la base de datos la colección con el nuevo algoritmo
        return coleccionOutputMapper.map(coleccionPersistida);
    }

    @Transactional
    public ColeccionOutputDto agregarFuenteAColeccion(String coleccionId, FuenteInputDto fuenteInputDto) throws ColeccionNoEncontradaException {
        Fuente fuente = fuenteService.obtenerFuentePorId(fuenteInputDto.getId());
        Coleccion coleccion = obtenerColeccion(coleccionId);
        coleccion.agregarFuente(fuente);
        coleccion = coleccionRepository.save(coleccion);

        // Publicar evento que se ejecutará después del commit de la transacción
        eventPublisher.publishEvent(new FuenteAgregadaAColeccionEvent(this, coleccionId, fuente.getId()));

        return coleccionOutputMapper.map(coleccion);
    }

    @Transactional
    public ColeccionOutputDto quitarFuenteDeColeccion(String idColeccion, String fuenteId) throws FuenteNoEncontradaException {
        Coleccion coleccion = obtenerColeccion(idColeccion);
        Fuente fuente = fuenteService.obtenerFuentePorId(fuenteId);

        coleccion.quitarFuente(fuente);
        coleccion = coleccionRepository.save(coleccion); // Updatea la colección después de quitar la fuente

        ColeccionOutputDto coleccionOutputDto = coleccionOutputMapper.map(coleccion);

        // Si en fuente por coleccion no quedan mas registros para esta fuente, entonces se eliminan las entradas de HechoXFuente que tengan este FuenteId
        if (!coleccionRepository.existsByFuenteId(fuenteId)) {
            fuente.eliminarTodosLosHechos();
            fuenteService.guardarFuente(fuente);
        }

        // Quitamos de HechoXColeccion aquellos hechos que eran de esta fuente
        hechoXColeccionRepository.deleteAllByFuenteId(fuenteId); // Eliminar hechos asociados a la fuente de la colección

        return coleccionOutputDto;
    }

    public List<String> obtenerAutocompletado(String currentSearch, Integer limit) {
        List<String> encontrados = currentSearch.length() >= 3 ? coleccionRepository.findAutocompletado(currentSearch, limit) : coleccionRepository.findAutocompletadoLike(currentSearch, limit);
        if(encontrados.isEmpty() && currentSearch.length() >= 3){
            return coleccionRepository.findAutocompletadoLike(currentSearch, limit);
        }
        else
            return encontrados;
    }
}