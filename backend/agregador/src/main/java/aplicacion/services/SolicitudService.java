package aplicacion.services;

import java.util.List;

import aplicacion.dto.input.SolicitudInputDto;
import aplicacion.dto.mappers.SolicitudOutputMapper;
import aplicacion.dto.output.SolicitudOutputDto;
import aplicacion.excepciones.HechoNoEncontradoException;
import aplicacion.excepciones.MotivoSolicitudException;
import aplicacion.domain.hechos.Hecho;
import aplicacion.repositorios.RepositorioDeSolicitudes;
import aplicacion.domain.solicitudes.*;
import aplicacion.domain.usuarios.Contribuyente;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class SolicitudService {

    private final RepositorioDeSolicitudes repositorioDeSolicitudes;
    private final HechoService hechoService;
    private final ContribuyenteService contribuyenteService;
    private final SolicitudOutputMapper solicitudOutputMapper;

    public SolicitudService(RepositorioDeSolicitudes repositorioDeSolicitudes, HechoService hechoService, ContribuyenteService contribuyenteService, SolicitudOutputMapper solicitudOutputMapper) {
        this.repositorioDeSolicitudes = repositorioDeSolicitudes;
        this.hechoService = hechoService;
        this.contribuyenteService = contribuyenteService;
        this.solicitudOutputMapper = solicitudOutputMapper;
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

    public SolicitudOutputDto obtenerSolicitudDTO(Long id) {
        return repositorioDeSolicitudes.findById(id)
                .map(solicitudOutputMapper::map)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada con ID: " + id));
    }

    public Page<SolicitudOutputDto> obtenerSolicitudesDTO(Pageable pageable) {
        return repositorioDeSolicitudes.findAll(pageable).map(solicitudOutputMapper::map);
    }
    public List<SolicitudEliminacion> obtenerSolicitudes() {
        return repositorioDeSolicitudes.findAll();
    }

    @Transactional
    public SolicitudEliminacion guardarSolicitud(SolicitudEliminacion solicitud) throws HechoNoEncontradoException {
        Hecho hecho = hechoService.obtenerHechoPorId(solicitud.getHecho().getId());
        //Contribuyente solicitante = contribuyenteService.obtenerContribuyentePorId(solicitud.getSolicitante().getId());
        //solicitud.setSolicitante(solicitante);
        //solicitante.agregarSolicitudEliminacion(solicitud);
        SolicitudEliminacion solicitudGuardada = repositorioDeSolicitudes.save(solicitud);
        hecho.agregarASolicitudes(solicitud);
        hechoService.guardarHecho(hecho);
        return solicitudGuardada;
    }

    public void save(SolicitudEliminacion sol){
        repositorioDeSolicitudes.save(sol);
    }

    @Transactional
    public SolicitudOutputDto guardarSolicitudDto(SolicitudInputDto solicitudDto) throws MotivoSolicitudException , HechoNoEncontradoException{
        this.validarMotivoSolicitud(solicitudDto.getMotivo());
        Hecho hecho = hechoService.obtenerHechoPorId(solicitudDto.getHechoId());
        Contribuyente contribuyente = contribuyenteService.obtenerContribuyentePorId(solicitudDto.getSolicitanteId());
        SolicitudEliminacion solicitud = new SolicitudEliminacion(contribuyente, hecho, solicitudDto.getMotivo());
        contribuyente.agregarSolicitudEliminacion(solicitud);
        return solicitudOutputMapper.map(guardarSolicitud(solicitud));
    }

    private void validarMotivoSolicitud(String motivo) throws MotivoSolicitudException {
        if (motivo.length() < 500) {
            throw new MotivoSolicitudException("EL motivo de la solicitud debe tener al menos 500 caracteres.");
        }
    }

    public void actualizarEstadoSolicitud(SolicitudEliminacion solicitud, String nuevoEstado, Contribuyente admin) {
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
                solicitud.aceptar(admin);
                break;
            case "RECHAZADA":
                solicitud.rechazar(admin);
                break;
            case "PRESCRIPTA":
                solicitud.prescribir();
                break;
            case "SPAM":
                solicitud.marcarSpam(admin);
                break;
            default:
                throw new IllegalArgumentException("Estado no válido: " + nuevoEstado);
        }
    }

}
