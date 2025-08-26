package domain.services;

import java.util.List;


import domain.dto.SolicitudDTO;
import domain.excepciones.MotivoSolicitudException;
import domain.hechos.Hecho;
import domain.repositorios.RepositorioDeSolicitudes;
import domain.solicitudes.*;
import domain.usuarios.Contribuyente;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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

    public List<SolicitudEliminacion> solicitudesRelacionadas(Long id) {
        SolicitudEliminacion sol;
        sol = repositorioDeSolicitudes.findById(id).orElse(null);
        if(sol == null) {
            throw new IllegalArgumentException("Solicitud no encontrada con ID: " + id);
        }

        return repositorioDeSolicitudes.findByHecho(sol.getHecho());
    }

    public SolicitudEliminacion obtenerSolicitud(Long id) {
        return repositorioDeSolicitudes.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada con ID: " + id));
    }

    public List<SolicitudEliminacion> obtenerSolicitudes() {
        return repositorioDeSolicitudes.findAll();
    }

    @Transactional
    public SolicitudEliminacion guardarSolicitud(SolicitudEliminacion solicitud) {
        Hecho hecho = hechoService.obtenerHechoPorId(solicitud.getHecho().getId());
        Contribuyente solicitante = contribuyenteService.obtenerContribuyentePorId(solicitud.getSolicitante().getId());
        solicitud.setSolicitante(solicitante);
        solicitante.agregarSolicitudEliminacion(solicitud);
        SolicitudEliminacion solicitudGuardada = repositorioDeSolicitudes.save(solicitud);
        hecho.agregarASolicitudes(solicitud);
        hechoService.guardarHecho(hecho);
        return solicitudGuardada;
    }

    @Transactional
    public SolicitudEliminacion guardarSolicitudDto(SolicitudDTO solicitudDto) throws MotivoSolicitudException {
        this.validarMotivoSolicitud(solicitudDto.getMotivo());
        Hecho hecho = hechoService.obtenerHechoPorId(solicitudDto.getHechoId());
        Contribuyente contribuyente = contribuyenteService.obtenerContribuyentePorId(Long.valueOf(solicitudDto.getSolicitanteId()));
        SolicitudEliminacion solicitud = new SolicitudEliminacion(contribuyente, hecho, solicitudDto.getMotivo());
        return guardarSolicitud(solicitud);
    }

    private void validarMotivoSolicitud(String motivo) throws MotivoSolicitudException {
        if (motivo.length() < 500) {
            throw new MotivoSolicitudException("EL motivo de la solicitud debe tener al menos 500 caracteres.");
        }
    }

    public void actualizarEstadoSolicitud(SolicitudEliminacion solicitud, String nuevoEstado) {
        //nuevoEstado = nuevoEstado.replace("\"", "").trim();
        // Se puede hacer eso para que se deba mandar el estado entre comillas.
        switch (nuevoEstado) {
            case "PENDIENTE":
                switch (solicitud.getEstado().getNombreEstado()) {
                    case "ACEPTADA":
                        solicitud.anularAceptacion();
                        break;
                    case "RECHAZADA":
                        solicitud.anularRechazo();
                        break;
                    case "PRESCRIPTA":
                        solicitud.anularPrescripcion();
                        break;
                    case "SPAM":
                        solicitud.anularMarcaSpam();
                        break;
                }
                break;
            case "ACEPTADA":
                solicitud.aceptar(null);
                break;
            case "RECHAZADA":
                solicitud.rechazar(null);
                break;
            case "PRESCRIPTA":
                solicitud.prescribir();
                break;
            case "SPAM":
                solicitud.marcarSpam(null);
                break;
            default:
                throw new IllegalArgumentException("Estado no válido: " + nuevoEstado);
        }
    }

}
