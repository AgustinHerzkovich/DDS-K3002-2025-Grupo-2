package aplicacion.services;

import aplicacion.domain.facts.FactSolicitud;
import aplicacion.domain.hechosYSolicitudes.solicitudes.SolicitudEliminacion;
import aplicacion.repositorios.agregador.SolicitudRepository;
import aplicacion.repositorios.olap.FactSolicitudRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CargaDeSolicitudesService {

    SolicitudRepository solicitudRepository;
    FactSolicitudRepository factSolicitudRepository;

    public CargaDeSolicitudesService(SolicitudRepository solicitudRepo, FactSolicitudRepository factSolicitudRepository) {
        this.solicitudRepository = solicitudRepo;
        this.factSolicitudRepository = factSolicitudRepository;
    }
    public void actualizarSolicitudes() {
        System.out.println("Actualizando Solicitudes...");
        factSolicitudRepository.deleteAll();
        Map<String,Long> mapa  = solicitudRepository.cantidadesEstados().stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],                             // clave: dtype
                        row -> ((Number) row[1]).longValue()                // valor: count
                ));
        Set<FactSolicitud> s = mapa.entrySet().stream().map(estadoYCantidad -> new FactSolicitud(estadoYCantidad.getKey(), estadoYCantidad.getValue())).collect(Collectors.toSet());
        s.forEach(System.out::println);
        factSolicitudRepository.saveAll(s);
        System.out.println("Solicitudes actualizadas");
    }
}
