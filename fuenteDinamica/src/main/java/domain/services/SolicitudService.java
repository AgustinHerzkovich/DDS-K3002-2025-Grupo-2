package domain.services;

import domain.repositorios.RepositorioDeSolicitudes;
import domain.solicitudes.SolicitudEliminacion;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SolicitudService {
    private final RepositorioDeSolicitudes repositorioDeSolicitudes;

    public SolicitudService(RepositorioDeSolicitudes repositorioDeSolicitudes) {
        this.repositorioDeSolicitudes = repositorioDeSolicitudes;
    }

    public void guardarSolicitud(SolicitudEliminacion solicitud) {
        repositorioDeSolicitudes.save(solicitud);
    }

    public List<SolicitudEliminacion> obtenerSolicitudes() {
        return repositorioDeSolicitudes.findAll();
    }

    public SolicitudEliminacion obtenerSolicitud(Long id) {
        return repositorioDeSolicitudes.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada con ID: " + id));
    }

    public void eliminarSolicitud(Long id) {
        if (repositorioDeSolicitudes.existsById(id)) {
            repositorioDeSolicitudes.deleteById(id);
        } else {
            throw new NoSuchElementException("Solicitud no encontrada con ID: " + id);
        }
    }
}
