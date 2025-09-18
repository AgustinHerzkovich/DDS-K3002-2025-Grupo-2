package aplicacion.services.scheduler;


import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ActualizacionEstadisticasScheduler {

    public ActualizacionEstadisticasScheduler(){

    }
    //todo revisar cuando lo ponemos, si calculamos el tiempo o si lo hacemos todos los dias a una hora
    @Scheduled(cron = "0 0 1 * * SUN")
    public void actualizarEstadisticas(){

    }


}




