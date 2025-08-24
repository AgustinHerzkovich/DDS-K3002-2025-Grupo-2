package domain.services;

import domain.dto.SolicitudDTO;
import domain.hechos.Hecho;
import domain.mappers.SolicitudMapper;
import domain.repositorios.RepositorioDeSolicitudes;
import domain.solicitudes.SolicitudEliminacion;
import domain.usuarios.Contribuyente;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SolicitudService {
    private final RepositorioDeSolicitudes repositorioDeSolicitudes;
    private final HechoService hechoService;
    private final ContribuyenteService contribuyenteService;

    public SolicitudService(RepositorioDeSolicitudes repositorioDeSolicitudes, HechoService hechoService, ContribuyenteService contribuyenteService) {
        this.repositorioDeSolicitudes = repositorioDeSolicitudes;
        this.hechoService = hechoService;
        this.contribuyenteService = contribuyenteService;
    }

    public void guardarSolicitud(SolicitudEliminacion solicitud) {
        repositorioDeSolicitudes.save(solicitud);
    }

    public void guardarSolicitudDto(SolicitudDTO solicitudDto) {
        Hecho hecho = hechoService.obtenerHecho(solicitudDto.getHechoId());
        Contribuyente solicitante = contribuyenteService.obtenerContribuyente(solicitudDto.getSolicitanteId());
        SolicitudEliminacion solicitud = new SolicitudMapper().map(solicitudDto, solicitante, hecho);
        solicitante.agregarSolicitudEliminacion(solicitud);
        contribuyenteService.guardarContribuyente(solicitante);
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
