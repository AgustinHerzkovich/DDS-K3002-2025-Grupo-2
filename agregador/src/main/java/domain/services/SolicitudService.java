package domain.services;

import java.util.List;


import domain.colecciones.AlgoritmoConsenso;
import domain.colecciones.Coleccion;
import domain.repositorios.RepositorioDeSolicitudes;
import domain.solicitudes.*;
import org.springframework.stereotype.Service;

@Service
public class SolicitudService {

    private final RepositorioDeSolicitudes repositorioDeSolicitudes;

    public SolicitudService(RepositorioDeSolicitudes repositorioDeSolicitudes) {
        this.repositorioDeSolicitudes = repositorioDeSolicitudes;
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
        repositorioDeSolicitudes.save(solicitud);
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
