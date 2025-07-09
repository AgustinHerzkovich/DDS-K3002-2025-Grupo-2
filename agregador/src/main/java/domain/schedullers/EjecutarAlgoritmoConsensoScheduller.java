package domain.schedullers;

import domain.algoritmos.*;
import domain.colecciones.AlgoritmoConsenso;
import domain.colecciones.Coleccion;
import domain.colecciones.fuentes.Fuente;
import domain.hechos.Hecho;
import domain.repositorios.RepositorioDeHechosXColeccion;
import domain.services.ColeccionService;
import domain.services.HechoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class EjecutarAlgoritmoConsensoScheduller {
    private final HechoService hechoService;
    private final ColeccionService coleccionService;
    private final RepositorioDeHechosXColeccion repositorioDeHechosXColeccion;
    private Algoritmo algoritmo;

    public EjecutarAlgoritmoConsensoScheduller(HechoService hechoService, ColeccionService coleccionService, RepositorioDeHechosXColeccion repositorioDeHechosXColeccion) {
        this.hechoService = hechoService;
        this.coleccionService = coleccionService;
        this.repositorioDeHechosXColeccion = repositorioDeHechosXColeccion;
    }

    @Scheduled(cron = "0 0 3 * * *") // Se ejecuta a las 3 AM
    public void curarHechos() {
        List<Coleccion> colecciones = coleccionService.obtenerColecciones();
        for (Coleccion coleccion : colecciones) {
            AlgoritmoConsenso algoritmoConsenso = coleccion.getAlgoritmoConsenso();
            switch (algoritmoConsenso) {
                case IRRESTRICTO -> algoritmo = new AlgoritmoIrrestricto();
                case MAYORIA_SIMPLE -> algoritmo = new AlgoritmoMayoriaSimple();
                case MULTIPLES_MENCIONES -> algoritmo = new AlgoritmoMultiplesMenciones();
                case ABSOLUTO -> algoritmo = new AlgoritmoAbsoluto();
            }

            Map<Fuente,List<Hecho>> hechos = hechoService.obtenerHechosPorColeccionPorFuente(coleccion.getIdentificadorHandle());
            List<Hecho> hechosCurados = algoritmo.curarHechos(hechos);
            for (Hecho hecho : hechosCurados) {
                repositorioDeHechosXColeccion.update(hecho.getId(), true);
            }
        }
        // por cada coleccion me fijo su algoritmo
        // busco en el repositorio de hechos por coleccion y me fijo la cantidad de veces que aparecen
        // taggeo los hechos como consensuados/curados
    }
}