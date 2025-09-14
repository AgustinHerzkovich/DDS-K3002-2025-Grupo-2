package aplicacion.services;

import aplicacion.domain.algoritmos.*;
import aplicacion.domain.colecciones.Coleccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.FuenteId;
import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.input.ColeccionInputDto;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.mappers.ColeccionInputMapper;
import aplicacion.dto.mappers.ColeccionOutputMapper;
import aplicacion.dto.mappers.FuenteInputMapper;
import aplicacion.dto.mappers.HechoOutputMapper;
import aplicacion.dto.output.ColeccionOutputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.excepciones.ColeccionNoEncontradaException;
import aplicacion.excepciones.FuenteNoEncontradaException;
import aplicacion.repositorios.RepositorioDeColecciones;
import aplicacion.repositorios.RepositorioDeHechosXColeccion;
import aplicacion.repositorios.RepositorioDeHechosXFuente;
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
    private final RepositorioDeHechosXFuente repositorioDeHechosXFuente;
    private final ColeccionInputMapper coleccionInputMapper;
    private final ColeccionOutputMapper coleccionOutputMapper;
    private final HechoOutputMapper hechoOutputMapper;
    private final FuenteService fuenteService;
    private final FuenteInputMapper fuenteInputMapper;

    public ColeccionService(ColeccionInputMapper coleccionInputMapper, ColeccionOutputMapper coleccionOutputMapper, RepositorioDeColecciones repositorioDeColecciones, HechoService hechoService, RepositorioDeHechosXColeccion repositorioDeHechosXColeccion, RepositorioDeHechosXFuente repositorioDeHechosXFuente, HechoOutputMapper hechoOutputMapper, FuenteService fuenteService, FuenteInputMapper fuenteInputMapper) {
        this.repositorioDeColecciones = repositorioDeColecciones;
        this.hechoService = hechoService;
        this.repositorioDeHechosXColeccion = repositorioDeHechosXColeccion;
        this.repositorioDeHechosXFuente = repositorioDeHechosXFuente;
        this.coleccionInputMapper = coleccionInputMapper;
        this.coleccionOutputMapper = coleccionOutputMapper;
        this.hechoOutputMapper = hechoOutputMapper;
        this.fuenteService = fuenteService;
        this.fuenteInputMapper = fuenteInputMapper;
    }

    public ColeccionOutputDto guardarColeccion(ColeccionInputDto coleccion) {
        Coleccion coleccionLocal = repositorioDeColecciones.save(coleccionInputMapper.map(coleccion));
        return coleccionOutputMapper.map(coleccionLocal);
    }

    public List<ColeccionOutputDto> obtenerColeccionesDTO() { //ahora service devuelve todos en DTO. Se crean metodos nuevos de ser necesario.
        return obtenerColecciones().stream().map(coleccionOutputMapper::map).collect(Collectors.toList());
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

    public List<HechoOutputDto> obtenerHechosIrrestrictosPorColeccion(String idColeccion,
                                                                      String categoria_buscada,
                                                                      LocalDateTime fechaReporteDesde,
                                                                      LocalDateTime fechaReporteHasta,
                                                                      LocalDateTime fechaAcontecimientoDesde,
                                                                      LocalDateTime fechaAcontecimientoHasta,
                                                                      Double latitud,
                                                                      Double longitud) {
        List<Hecho> hechosIrrestrictos = hechoService.obtenerHechosPorColeccion(idColeccion);

        return filtrarHechosQueryParam(hechosIrrestrictos, categoria_buscada, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud);
    }

    public List<HechoOutputDto> obtenerHechosCuradosPorColeccionDTO(String idColeccion,
                                                                    String categoria_buscada,
                                                                    LocalDateTime fechaReporteDesde,
                                                                    LocalDateTime fechaReporteHasta,
                                                                    LocalDateTime fechaAcontecimientoDesde,
                                                                    LocalDateTime fechaAcontecimientoHasta,
                                                                    Double latitud,
                                                                    Double longitud) {
        List<Hecho> hechosCurados = hechoService.obtenerHechosCuradosPorColeccion(idColeccion);

        return filtrarHechosQueryParam(hechosCurados, categoria_buscada, fechaReporteDesde, fechaReporteHasta, fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud);
    }

    public List<HechoOutputDto> filtrarHechosQueryParam(List<Hecho> hechos,
                                                        String categoria_buscada,
                                                        LocalDateTime fechaReporteDesde,
                                                        LocalDateTime fechaReporteHasta,
                                                        LocalDateTime fechaAcontecimientoDesde,
                                                        LocalDateTime fechaAcontecimientoHasta,
                                                        Double latitud,
                                                        Double longitud) {
        return hechos.stream()
                .filter(h -> categoria_buscada == null || h.getCategoria().getNombre().equalsIgnoreCase(categoria_buscada))
                .filter(h -> fechaReporteDesde == null ||  h.getFechaCarga().isAfter(fechaReporteDesde))
                .filter(h -> fechaReporteHasta == null || h.getFechaCarga().isBefore(fechaReporteHasta))
                .filter(h -> fechaAcontecimientoDesde == null || h.getFechaAcontecimiento().isAfter(fechaAcontecimientoDesde))
                .filter(h -> fechaAcontecimientoHasta == null || h.getFechaAcontecimiento().isBefore(fechaAcontecimientoHasta))
                .filter(h -> latitud == null || h.getUbicacion().getLatitud().equals(latitud))
                .filter(h -> longitud == null || h.getUbicacion().getLongitud().equals(longitud))
                .map(hechoOutputMapper::map)
                .collect(Collectors.toList()); //convierte el stream de elementos (después de aplicar los .filter(...), .map(...), etc.) en una lista (List<T>) de resultados.
    }

    public void eliminarColeccion(String idColeccion) {
        Coleccion coleccion = repositorioDeColecciones.findById(idColeccion)
                .orElseThrow(() -> new IllegalArgumentException("Colección no encontrada con ID: " + idColeccion));
        hechoService.borrarHechosPorColeccion(coleccion);
        repositorioDeColecciones.delete(coleccion);
        System.out.println("Colección eliminada: " + idColeccion);
    }

    public void modificarAlgoritmoDeColeccion(String idColeccion, String nuevoAlgoritmo) {
        Coleccion coleccion = repositorioDeColecciones.findById(idColeccion)
                .orElseThrow(() -> new IllegalArgumentException("Colección no encontrada con ID: " + idColeccion));
        coleccion.setAlgoritmoConsenso(crearAlgoritmoDesdeNombre(nuevoAlgoritmo));
        repositorioDeColecciones.save(coleccion); // Updatea en la base de datos la colección con el nuevo algoritmo
    }

    private AlgoritmoConsenso crearAlgoritmoDesdeNombre(String nombre) {
        return switch (nombre) {
            case "irrestricto" -> new AlgoritmoConsensoIrrestricto();
            case "absoluto" -> new AlgoritmoConsensoAbsoluto();
            case "mayoriaSimple" -> new AlgoritmoConsensoMayoriaSimple();
            case "multiplesMenciones" -> new AlgoritmoConsensoMultiplesMenciones();
            default -> throw new IllegalArgumentException("Algoritmo desconocido: " + nombre);
        };
    }

    public ColeccionOutputDto agregarFuenteAColeccion(String coleccionId, FuenteInputDto fuenteInputDto) throws ColeccionNoEncontradaException {
        Fuente fuente = fuenteInputMapper.map(fuenteInputDto);
        fuenteService.guardarFuente(fuente);
        Coleccion coleccion = obtenerColeccion(coleccionId);
        coleccion.agregarFuente(fuente);
        coleccion = repositorioDeColecciones.save(coleccion);
        return coleccionOutputMapper.map(coleccion);
    }

    @Transactional
    public ColeccionOutputDto quitarFuenteDeColeccion(String idColeccion, FuenteId fuenteId) throws FuenteNoEncontradaException {
        Coleccion coleccion = obtenerColeccion(idColeccion);
        Fuente fuente = fuenteService.obtenerFuentePorId(fuenteId);

        coleccion.quitarFuente(fuente);
        coleccion = repositorioDeColecciones.save(coleccion); // Updatea la colección después de quitar la fuente

        ColeccionOutputDto coleccionOutputDto = coleccionOutputMapper.map(coleccion);

        // Si en fuente por coleccion no quedan mas registros para esta fuente, entonces se eliminan las entradas de HechoXFuente que tengan este FuenteId
        if (!repositorioDeColecciones.existsByFuenteId(fuenteId)) {
            repositorioDeHechosXFuente.deleteAllByFuenteId(fuenteId);
        }

        // Quitamos de HechoXColeccion aquellos hechos que eran de esta fuente
        repositorioDeHechosXColeccion.deleteAllByFuenteId(fuenteId); // Eliminar hechos asociados a la fuente de la colección

        return coleccionOutputDto;
    }
}