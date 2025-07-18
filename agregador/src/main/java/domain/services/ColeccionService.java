package domain.services;

import domain.algoritmos.*;
import domain.colecciones.Coleccion;
import domain.colecciones.fuentes.Fuente;
import domain.colecciones.fuentes.FuenteId;
import domain.colecciones.fuentes.FuenteXColeccion;
import domain.hechos.Hecho;
import domain.repositorios.RepositorioDeColecciones;
import domain.repositorios.RepositorioDeFuentesXColeccion;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ColeccionService {
    private final RepositorioDeColecciones repositorioDeColecciones;
    private final RepositorioDeFuentesXColeccion repositorioDeFuentesXColeccion;
    private final HechoService hechoService;
    
    public ColeccionService(RepositorioDeColecciones repositorioDeColecciones, RepositorioDeFuentesXColeccion repositorioDeFuentesXColeccion, HechoService hechoService) {
        this.repositorioDeColecciones = repositorioDeColecciones;
        this.repositorioDeFuentesXColeccion = repositorioDeFuentesXColeccion;
        this.hechoService = hechoService;
    }

    public void guardarColeccion(Coleccion coleccion) {
        repositorioDeColecciones.save(coleccion);
    }

    public List<Coleccion> obtenerColecciones() {
        return repositorioDeColecciones.findAll();
    }

    public Coleccion obtenerColeccion(String idColeccion) {
        return repositorioDeColecciones.findById(idColeccion).orElseThrow(() -> new IllegalArgumentException("Colección no encontrada con ID: " + idColeccion));
    }

    public List<Hecho> obtenerHechosIrrestrictosPorColeccion(String idColeccion,
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

    public List<Hecho> obtenerHechosCuradosPorColeccion(String idColeccion,
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

    public List<Hecho> filtrarHechosQueryParam(List<Hecho> hechos,
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
                .collect(Collectors.toList()); //convierte el stream de elementos (después de aplicar los .filter(...), .map(...), etc.) en una lista (List<T>) de resultados.
    }

    public void eliminarColeccion(String idColeccion) {
        Coleccion coleccion = repositorioDeColecciones.findById(idColeccion)
                .orElseThrow(() -> new IllegalArgumentException("Colección no encontrada con ID: " + idColeccion));
        repositorioDeColecciones.delete(coleccion);
    }

    public void guardarFuentesPorColeccion(Coleccion coleccion, List<Fuente> fuentes) {
        for (Fuente fuente : fuentes) {
            FuenteXColeccion fuentePorColeccion = new FuenteXColeccion(fuente, coleccion);
            repositorioDeFuentesXColeccion.save(fuentePorColeccion);
        }
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

    public void agregarFuenteAColeccion(Coleccion coleccion, Fuente fuente) {
        coleccion.agregarFuente(fuente);

        FuenteXColeccion fuentePorColeccion = new FuenteXColeccion(fuente, coleccion);
        repositorioDeFuentesXColeccion.save(fuentePorColeccion);
    }

    public void quitarFuenteDeColeccion(Coleccion coleccion, FuenteId fuenteId) {
        Optional<FuenteXColeccion> fuenteXColeccionOpt = repositorioDeFuentesXColeccion.findByFuenteIdAndColeccion(fuenteId, coleccion);

        fuenteXColeccionOpt.ifPresent(fxc -> {
            Fuente fuente = fxc.getFuente();
            coleccion.quitarFuente(fuente);
            repositorioDeFuentesXColeccion.delete(fxc);
        });

        guardarColeccion(coleccion); // Updateo la colección después de quitar la fuente

        fuenteXColeccionOpt.ifPresent(repositorioDeFuentesXColeccion::delete);
    }
}