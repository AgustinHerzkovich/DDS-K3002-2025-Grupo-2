package aplicacion.services;

import aplicacion.domain.algoritmos.*;
import aplicacion.domain.colecciones.Coleccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.FuenteId;
import aplicacion.clasesIntermedias.FuenteXColeccion;
import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.input.ColeccionInputDTO;
import aplicacion.dto.mappers.ColeccionInputMapper;
import aplicacion.dto.mappers.ColeccionOutputMapper;
import aplicacion.dto.mappers.HechoOutputMapper;
import aplicacion.dto.output.ColeccionOutputDTO;
import aplicacion.dto.output.HechoOutputDTO;
import aplicacion.excepciones.ColeccionNoEncontradaException;
import aplicacion.repositorios.RepositorioDeColecciones;
import aplicacion.repositorios.RepositorioDeFuentesXColeccion;
import aplicacion.repositorios.RepositorioDeHechosXColeccion;
import aplicacion.repositorios.RepositorioDeHechosXFuente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ColeccionService {
    private final RepositorioDeColecciones repositorioDeColecciones;
    private final RepositorioDeFuentesXColeccion repositorioDeFuentesXColeccion;
    private final HechoService hechoService;
    private final RepositorioDeHechosXColeccion repositorioDeHechosXColeccion;
    private final RepositorioDeHechosXFuente repositorioDeHechosXFuente;
    private final ColeccionInputMapper coleccionInputMapper;
    private final ColeccionOutputMapper coleccionOutputMapper;

    public ColeccionService(ColeccionInputMapper coleccionInputMapper, ColeccionOutputMapper coleccionOutputMapper, RepositorioDeColecciones repositorioDeColecciones, RepositorioDeFuentesXColeccion repositorioDeFuentesXColeccion, HechoService hechoService, RepositorioDeHechosXColeccion repositorioDeHechosXColeccion, RepositorioDeHechosXFuente repositorioDeHechosXFuente) {
        this.repositorioDeColecciones = repositorioDeColecciones;
        this.repositorioDeFuentesXColeccion = repositorioDeFuentesXColeccion;
        this.hechoService = hechoService;
        this.repositorioDeHechosXColeccion = repositorioDeHechosXColeccion;
        this.repositorioDeHechosXFuente = repositorioDeHechosXFuente;
        this.coleccionInputMapper = coleccionInputMapper;
        this.coleccionOutputMapper = coleccionOutputMapper;
    }

    public ColeccionOutputDTO guardarColeccion(ColeccionInputDTO coleccion) {
        Coleccion coleccionLocal = repositorioDeColecciones.save(coleccionInputMapper.map(coleccion));
        System.out.println(coleccionLocal.getId());
        return coleccionOutputMapper.map(coleccionLocal);
    }

    public List<ColeccionOutputDTO> obtenerColeccionesDTO() { //ahora service devuelve todos en DTO. Se crean metodos nuevos de ser necesario.
        return obtenerColecciones().stream().map(coleccionOutputMapper::map).collect(Collectors.toList());
    }

    public List<Coleccion> obtenerColecciones() { //ahora service devuelve todos en DTO. Se crean metodos nuevos de ser necesario.
        return repositorioDeColecciones.findAll();
    }

    public Coleccion obtenerColeccion(String idColeccion) {
        return repositorioDeColecciones.findById(idColeccion).orElseThrow(() -> new IllegalArgumentException("Colección no encontrada con ID: " + idColeccion));
    }

    public ColeccionOutputDTO obtenerColeccionDTO(String idColeccion) {
         return repositorioDeColecciones.findById(idColeccion)
                .map(coleccionOutputMapper::map)
                .orElseThrow(() -> new IllegalArgumentException("Colección no encontrada con ID: " + idColeccion));
    }

    public List<HechoOutputDTO> obtenerHechosIrrestrictosPorColeccion(String idColeccion,
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

    public List<HechoOutputDTO> obtenerHechosCuradosPorColeccionDTO(String idColeccion,
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

    public List<HechoOutputDTO> filtrarHechosQueryParam(List<Hecho> hechos,
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
                .map(HechoOutputMapper::map)
                .collect(Collectors.toList()); //convierte el stream de elementos (después de aplicar los .filter(...), .map(...), etc.) en una lista (List<T>) de resultados.
    }

    public void eliminarColeccion(String idColeccion) {
        Coleccion coleccion = repositorioDeColecciones.findById(idColeccion)
                .orElseThrow(() -> new IllegalArgumentException("Colección no encontrada con ID: " + idColeccion));
        borrarFuentesPorColeccion(coleccion);
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

    public void agregarFuenteAColeccion(String coleccionId, Fuente fuente) throws ColeccionNoEncontradaException {
        Coleccion coleccion = obtenerColeccion(coleccionId);
        coleccion.agregarFuente(fuente);
        repositorioDeColecciones.save(coleccion);
        FuenteXColeccion fuentePorColeccion = new FuenteXColeccion(fuente, coleccion);
        repositorioDeFuentesXColeccion.save(fuentePorColeccion);
    }

    @Transactional
    public void quitarFuenteDeColeccion(String idColeccion, FuenteId fuenteId) {
        Coleccion coleccion = obtenerColeccion(idColeccion);
        Optional<FuenteXColeccion> fuenteXColeccionOpt = repositorioDeFuentesXColeccion.findByFuenteIdAndColeccion(fuenteId, coleccion);

        fuenteXColeccionOpt.ifPresent(fxc -> {
            Fuente fuente = fxc.getFuente();
            coleccion.quitarFuente(fuente);
            repositorioDeFuentesXColeccion.delete(fxc);
            repositorioDeColecciones.save(coleccion); // Updatea la colección después de quitar la fuente
        });

        // Si en fuente por coleccion no quedan mas registros para esta fuente, entonces se eliminan las entradas de HechoXFuente que tengan este FuenteId
        if (!repositorioDeFuentesXColeccion.existsByFuenteId(fuenteId)) {
            repositorioDeHechosXFuente.deleteAllByFuenteId(fuenteId);
        }

        // Quitamos de HechoXColeccion aquellos hechos que eran de esta fuente
        repositorioDeHechosXColeccion.deleteAllByFuenteId(fuenteId); // Eliminar hechos asociados a la fuente de la colección
    }
    public void borrarFuentesPorColeccion(Coleccion coleccion) {
        repositorioDeFuentesXColeccion.deleteAllByColeccionId(coleccion.getId());
    }
}