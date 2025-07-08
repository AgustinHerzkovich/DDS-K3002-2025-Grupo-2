package domain.services;

import domain.colecciones.Coleccion;
import domain.colecciones.fuentes.Fuente;
import domain.hechos.Hecho;
import domain.repositorios.RepositorioDeColecciones;
import domain.repositorios.RepositorioDeFuentesXColeccion;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
                .filter(h -> fechaReporteDesde == null ||  h.seCargoDespuesDe(fechaReporteDesde))
                .filter(h -> fechaReporteHasta == null || h.seCargoAntesDe(fechaReporteHasta))
                .filter(h -> fechaAcontecimientoDesde == null || h.ocurrioDespuesDe(fechaAcontecimientoDesde))
                .filter(h -> fechaAcontecimientoHasta == null || h.ocurrioAntesDe(fechaAcontecimientoHasta))
                .filter(h -> latitud == null || h.getUbicacion().getLatitud().equals(latitud))
                .filter(h -> longitud == null || h.getUbicacion().getLongitud().equals(longitud))
                .collect(Collectors.toList()); //convierte el stream de elementos (después de aplicar los .filter(...), .map(...), etc.) en una lista (List<T>) de resultados.
    }
    public void eliminarColeccion(String idColeccion) {
        Coleccion coleccion = repositorioDeColecciones.findById(idColeccion)
                .orElseThrow(() -> new IllegalArgumentException("Colección no encontrada con ID: " + idColeccion));
        repositorioDeColecciones.delete(coleccion);
    }

    public void guardarFuentesPorColeccion(String identificadorHandle, List<Fuente> fuentes) {
        // TODO: Implementar esto
    }
}
