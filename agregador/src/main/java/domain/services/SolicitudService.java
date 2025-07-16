package domain.services;

import java.util.List;


import java.util.Optional;
import domain.hechos.Hecho;
import domain.repositorios.RepositorioDeContribuyentes;
import domain.repositorios.RepositorioDeSolicitudes;
import domain.solicitudes.*;
import domain.usuarios.Contribuyente;
import org.springframework.stereotype.Service;

@Service
public class SolicitudService {

    private final RepositorioDeSolicitudes repositorioDeSolicitudes;
    private final RepositorioDeContribuyentes repositorioDeContribuyentes;
    private final HechoService hechoService;

    public SolicitudService(RepositorioDeSolicitudes repositorioDeSolicitudes, HechoService hechoService, RepositorioDeContribuyentes repositorioDeContribuyentes) {
        this.repositorioDeSolicitudes = repositorioDeSolicitudes;
        this.hechoService = hechoService;
        this.repositorioDeContribuyentes = repositorioDeContribuyentes;
    }

    public List<SolicitudEliminacion> solicitudesRelacionadas(Long id) {
        SolicitudEliminacion sol;
        sol = repositorioDeSolicitudes.findById(id).orElse(null);
        if(sol == null) {
            throw new IllegalArgumentException("Solicitud no encontrada con ID: " + id);
        }

        List <SolicitudEliminacion> solicitudesADevolver = repositorioDeSolicitudes.findByHechoId(sol.getHecho().getId());
        solicitudesADevolver.add(sol);
        return solicitudesADevolver;
    }

    public void guardarSolicitud(SolicitudEliminacion solicitud) {
        Hecho hecho = hechoService.obtenerHechoPorId(solicitud.getHecho().getId());
        solicitud.setHecho(hecho);
        // Soy consciente que deberia haber un contribuyente service. Si lees esto, crealo vos
        Contribuyente solicitante = solicitud.getSolicitante();
        Optional<Contribuyente> existente = repositorioDeContribuyentes.findById(solicitante.getContribuyenteId());
        if (existente.isPresent()) {
            solicitante = existente.get();
        } else {
            solicitante = repositorioDeContribuyentes.save(solicitante);
        }
        solicitud.setSolicitante(solicitante);
        repositorioDeSolicitudes.save(solicitud);
        hecho.agregarASolicitudes(solicitud);
        hechoService.guardarHecho(hecho);
    }

    public void actualizarEstadoSolicitud(SolicitudEliminacion solicitud, String nuevoEstado) {
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
