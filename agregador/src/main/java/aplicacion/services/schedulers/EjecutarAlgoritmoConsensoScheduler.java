package aplicacion.services.schedulers;

import aplicacion.domain.algoritmos.AlgoritmoConsenso;
import aplicacion.domain.colecciones.Coleccion;
import aplicacion.domain.colecciones.HechoXColeccion;
import aplicacion.domain.colecciones.HechoXColeccionId;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.repositorios.RepositorioDeHechosXColeccion;
import aplicacion.services.ColeccionService;
import aplicacion.services.HechoService;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.annotation.SchedulingConfigurer;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Configuration
@EnableScheduling
public class EjecutarAlgoritmoConsensoScheduler implements SchedulingConfigurer {
    private final HechoService hechoService;
    private final ColeccionService coleccionService;
    private final RepositorioDeHechosXColeccion repositorioDeHechosXColeccion;
    @Setter
    private volatile Integer horaBajaCarga = 3; // Por default es a las 3 AM

    public EjecutarAlgoritmoConsensoScheduler(HechoService hechoService, ColeccionService coleccionService, RepositorioDeHechosXColeccion repositorioDeHechosXColeccion) {
        this.hechoService = hechoService;
        this.coleccionService = coleccionService;
        this.repositorioDeHechosXColeccion = repositorioDeHechosXColeccion;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // trigger dinámico
        taskRegistrar.addTriggerTask(
                this::curarHechos, // tarea a ejecutar
                triggerContext -> {
                    Calendar nextExecution = Calendar.getInstance();
                    nextExecution.setTime(new Date());
                    nextExecution.set(Calendar.HOUR_OF_DAY, horaBajaCarga);
                    nextExecution.set(Calendar.MINUTE, 0);
                    nextExecution.set(Calendar.SECOND, 0);

                    Date lastActualExecution = triggerContext.lastActualExecutionTime();
                    if (lastActualExecution != null && nextExecution.getTime().before(lastActualExecution)) {
                        nextExecution.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    return nextExecution.getTime().toInstant();
                }
        );
    }

    public void curarHechos() {
        System.out.println("Se ha iniciado la curación de hechos. Esto puede tardar un rato.");
        List<Coleccion> colecciones = coleccionService.obtenerColecciones();
        for (Coleccion coleccion : colecciones) {
            AlgoritmoConsenso algoritmoConsenso = coleccion.getAlgoritmoConsenso();

            Map<Fuente,List<Hecho>> hechos = hechoService.obtenerHechosPorColeccionPorFuente(coleccion.getIdentificadorHandle());
            List<Hecho> hechosCurados = algoritmoConsenso.curarHechos(hechos);
            for (Hecho hecho : hechosCurados) {
                HechoXColeccion hechoXColeccion = repositorioDeHechosXColeccion.findById(new HechoXColeccionId(hecho.getId(), coleccion.getIdentificadorHandle()))
                        .orElseThrow(() -> new RuntimeException("No existe la relación"));
                hechoXColeccion.setConsensuado(true);
                repositorioDeHechosXColeccion.save(hechoXColeccion);
            }
        }
        System.out.println("Curación de hechos finalizada.");
        // por cada coleccion me fijo su algoritmo
        // busco en el repositorio de hechos por coleccion y me fijo la cantidad de veces que aparecen
        // taggeo los hechos como consensuados/curados
    }
}