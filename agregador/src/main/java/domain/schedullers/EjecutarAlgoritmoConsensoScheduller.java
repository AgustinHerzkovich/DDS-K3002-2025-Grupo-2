package domain.schedullers;

import domain.algoritmos.*;
import domain.colecciones.AlgoritmoConsenso;
import domain.colecciones.Coleccion;
import domain.hechos.Hecho;
import domain.repositorios.RepositorioHechosXColeccion;
import domain.services.ColeccionService;
import domain.services.HechoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EjecutarAlgoritmoConsensoScheduller {
    private final HechoService hechoService;
    private final ColeccionService coleccionService;
    private final RepositorioHechosXColeccion respositorioDeHechosXColeccion;
    private Algoritmo algoritmo;

    public EjecutarAlgoritmoConsensoScheduller(HechoService hechoService, ColeccionService coleccionService, RepositorioHechosXColeccion respositorioDeHechosXColeccion) {
        this.hechoService = hechoService;
        this.coleccionService = coleccionService;
        this.respositorioDeHechosXColeccion = respositorioDeHechosXColeccion;
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

            // TODO: aqui deberia retornar la lista de tuplas para luego pasarsela al algoritmo y que haga todos los calculos
            List<Hecho> hechos = hechoService.obtenerHechosPorColeccion(coleccion.getIdentificadorHandle()); // todo TESTEAR EL SELECT
            List<Hecho> hechosCurados = algoritmo.curarHechos(hechos); // TODO: seguir desde acá
            repositorioHechosXColeccion.update(hechosCurados);
        }
        // por cada coleccion me fijo su algoritmo
        // busco en el repositorio de hechos por coleccion y me fijo la cantidad de veces que aparecen
        // taggeo los hechos como consensuados/curados
    }
}
//findByColeccionId(Long idColeccion) {
// SELECT * FROM hechos
// JOIN hechos_x_coleccion ON hechos.id = hechos_x_coleccion.hecho_id
// WHERE hechos_x_coleccion.coleccion_id = idColeccion
//}