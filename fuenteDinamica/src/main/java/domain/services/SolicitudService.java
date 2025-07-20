package domain.services;

import domain.dto.SolicitudDTO;
import domain.hechos.Hecho;
import domain.mappers.SolicitudMapper;
import domain.repositorios.RepositorioDeHechos;
import domain.repositorios.RepositorioDeSolicitudes;
import domain.solicitudes.SolicitudEliminacion;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SolicitudService {
    private final RepositorioDeSolicitudes repositorioDeSolicitudes;
    private final HechoService hechoService;

    public SolicitudService(RepositorioDeSolicitudes repositorioDeSolicitudes, HechoService hechoService) {
        this.repositorioDeSolicitudes = repositorioDeSolicitudes;
        this.hechoService = hechoService;
    }

    public void guardarSolicitud(SolicitudEliminacion solicitud) {
        repositorioDeSolicitudes.save(solicitud);
    }

    public void guardarSolicitudDto(SolicitudDTO solicitudDto) {
        Hecho hecho = hechoService.obtenerHecho(solicitudDto.getHechoId());
        SolicitudEliminacion solicitud = new SolicitudMapper().map(solicitudDto, hecho);
        guardarSolicitud(solicitud);
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
