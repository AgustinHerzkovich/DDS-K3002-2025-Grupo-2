package aplicacion.services;

import aplicacion.domain.hechos.Ubicacion;
import aplicacion.excepciones.UbicacionNoEncontradaException;
import aplicacion.repositorios.RepositorioDeUbicaciones;
import org.springframework.stereotype.Service;

@Service
public class UbicacionService {
    private final RepositorioDeUbicaciones repositorioDeUbicaciones;

    public UbicacionService(RepositorioDeUbicaciones repositorioDeUbicaciones) {
        this.repositorioDeUbicaciones = repositorioDeUbicaciones;
    }

    public Ubicacion obtenerUbicacionPorLatitudYLongitud(Double latitud, Double longitud) throws UbicacionNoEncontradaException {
        return repositorioDeUbicaciones.findByLatitudAndLongitud(latitud, longitud).orElseThrow(() -> new UbicacionNoEncontradaException("Ubicación no encontrada con latitud: " + latitud + " y longitud: " + longitud));
    }

    public Ubicacion agregarUbicacion(Double latitud, Double longitud) {
        return repositorioDeUbicaciones.save(new Ubicacion(latitud, longitud));
    }
}
