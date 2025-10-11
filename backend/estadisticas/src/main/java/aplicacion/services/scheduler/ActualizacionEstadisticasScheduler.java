package aplicacion.services.scheduler;


import aplicacion.services.CargaDeColeccionesService;
import aplicacion.services.CargaDeHechosService;
import aplicacion.services.CargaDeSolicitudesService;
import aplicacion.services.CargaDeSolicitudesService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ActualizacionEstadisticasScheduler {
    private CargaDeHechosService cargaDeHechosService;
    private CargaDeColeccionesService cargaDeColeccionesService;
    private CargaDeSolicitudesService cargaDeSolicitudesService;

    public ActualizacionEstadisticasScheduler(CargaDeHechosService cargaDeHechosService, CargaDeColeccionesService cargaDeColeccionesService, CargaDeSolicitudesService cargaDeSolicitudesService) {
        this.cargaDeHechosService = cargaDeHechosService;
        this.cargaDeColeccionesService = cargaDeColeccionesService;
        this.cargaDeSolicitudesService = cargaDeSolicitudesService;
    }

    //todo revisar cuando lo ponemos, si calculamos el tiempo o si lo hacemos todos los dias a una hora
    @Scheduled(cron = "0 0 1 * * SUN")
    public void actualizarEstadisticas() {
        cargaDeHechosService.actualizarHechos();
        cargaDeColeccionesService.actualizarColecciones();
        cargaDeSolicitudesService.actualizarSolicitudes();
    }
}




