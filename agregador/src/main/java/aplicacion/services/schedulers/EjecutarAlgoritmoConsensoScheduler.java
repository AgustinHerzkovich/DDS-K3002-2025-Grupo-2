package aplicacion.services.schedulers;

import aplicacion.domain.algoritmos.*;
import aplicacion.domain.colecciones.Coleccion;
import aplicacion.clasesIntermedias.HechoXColeccion;
import aplicacion.clasesIntermedias.HechoXColeccionId;
import aplicacion.domain.hechos.Hecho;
import aplicacion.repositorios.RepositorioDeHechosXColeccion;
import aplicacion.services.ColeccionService;
import aplicacion.services.FuenteService;
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
    private final FuenteService fuenteService;
    private final RepositorioDeHechosXColeccion repositorioDeHechosXColeccion;
    @Setter
    private volatile Integer horaBajaCarga = 3; // Por default es a las 3 AM

    public EjecutarAlgoritmoConsensoScheduler(HechoService hechoService, ColeccionService coleccionService, RepositorioDeHechosXColeccion repositorioDeHechosXColeccion, FuenteService fuenteService) {
        this.hechoService = hechoService;
        this.coleccionService = coleccionService;
        this.repositorioDeHechosXColeccion = repositorioDeHechosXColeccion;
        this.fuenteService = fuenteService;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                this::curarHechos,
                triggerContext -> {
                    Calendar nextExecution = Calendar.getInstance();
                    Date lastExecution = triggerContext.lastActualExecutionTime();

                    // Si nunca se ejecutó, arranca desde HOY a la hora configurada
                    if (lastExecution == null) {
                        nextExecution.set(Calendar.HOUR_OF_DAY, horaBajaCarga);
                        nextExecution.set(Calendar.MINUTE, 0);
                        nextExecution.set(Calendar.SECOND, 0);

                        // Si ya pasó hoy, lo manda al día siguiente
                        if (nextExecution.getTime().before(new Date())) {
                            nextExecution.add(Calendar.DAY_OF_MONTH, 1);
                        }
                    } else {
                        // Si ya se ejecutó, calcula la siguiente ocurrencia (día siguiente a la hora fija)
                        nextExecution.setTime(lastExecution);
                        nextExecution.add(Calendar.DAY_OF_MONTH, 1);
                        nextExecution.set(Calendar.HOUR_OF_DAY, horaBajaCarga);
                        nextExecution.set(Calendar.MINUTE, 0);
                        nextExecution.set(Calendar.SECOND, 0);
                    }

                    return nextExecution.getTime().toInstant();
                }
        );
    }

    public void curarHechos() {
        System.out.println("Se ha iniciado la curación de hechos. Esto puede tardar un rato.");
        List<Coleccion> colecciones = coleccionService.obtenerColecciones();
        for (Coleccion coleccion : colecciones) {
            TipoAlgoritmoConsenso tipoAlgoritmoConsenso = coleccion.getTipoAlgoritmoConsenso();
            AlgoritmoConsenso algoritmoConsenso = null;

            switch(tipoAlgoritmoConsenso) {
                case IRRESTRICTO -> algoritmoConsenso = new AlgoritmoConsensoIrrestricto();
                case MAYORIA_SIMPLE -> algoritmoConsenso = new AlgoritmoConsensoMayoriaSimple();
                case MULTIPLES_MENCIONES -> algoritmoConsenso = new AlgoritmoConsensoMultiplesMenciones();
                case ABSOLUTO ->  algoritmoConsenso = new AlgoritmoConsensoAbsoluto();
                default -> throw new IllegalArgumentException("Tipo de algoritmo no reconocido: " + tipoAlgoritmoConsenso);
            }

            Map<Hecho, Long> conteoHechos = hechoService.contarHechosPorFuente(coleccion);
            Long totalFuentes = fuenteService.obtenerCantidadFuentes();

            List<Hecho> hechosCurados = algoritmoConsenso.curarHechos(conteoHechos, totalFuentes);
            for (Hecho hecho : hechosCurados) {
                HechoXColeccion hechoXColeccion = repositorioDeHechosXColeccion.findById(new HechoXColeccionId(hecho.getId(), coleccion.getId()))
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