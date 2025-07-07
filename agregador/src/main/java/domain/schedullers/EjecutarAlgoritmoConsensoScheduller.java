package domain.schedullers;

import domain.algoritmos.*;
import domain.colecciones.AlgoritmoConsenso;
import domain.colecciones.Coleccion;
import domain.hechos.Hecho;
import domain.repositorios.RepositorioDeColecciones;
import domain.repositorios.RepositorioDeHechos;
import domain.repositorios.RepositorioHechosXColeccion;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EjecutarAlgoritmoConsensoScheduller {
    private final RepositorioDeHechos repositorioDeHechos;
    private final RepositorioDeColecciones repositorioDeColecciones;
    private final RepositorioHechosXColeccion respositorioDeHechosXColeccion;
    private Algoritmo algoritmo;

    public EjecutarAlgoritmoConsensoScheduller(RepositorioDeHechos repositorioDeHechos, RepositorioDeColecciones repositorioDeColecciones, RepositorioHechosXColeccion respositorioDeHechosXColeccion) {
        this.repositorioDeHechos = repositorioDeHechos;
        this.repositorioDeColecciones = repositorioDeColecciones;
        this.respositorioDeHechosXColeccion = respositorioDeHechosXColeccion;
    }

    @Scheduled(cron = "0 0 3 * * *") // Se ejecuta a las 3 AM
    public void curarHechos() {
        List<Coleccion> colecciones = repositorioDeColecciones.findAll();
        for (Coleccion coleccion : colecciones) {
            AlgoritmoConsenso algoritmo_consenso = coleccion.getAlgoritmo_consenso();
            switch (algoritmo_consenso) {
                case IRRESTRICTO -> algoritmo = new AlgoritmoIrrestricto();
                case MAYORIA_SIMPLE -> algoritmo = new AlgoritmoMayoriaSimple();
                case MULTIPLES_MENCIONES -> algoritmo = new AlgoritmoMultiplesMenciones();
                case ABSOLUTO -> algoritmo = new AlgoritmoAbsoluto();
            }

            List<Hecho> hechos = repositorioDeHechos.findByColeccionId(coleccion.getIdentificador_handle()); // ESTE SUPUESTAMENTE SE CONECTA CON HECHOXCOLECCOIN PARA OBTENER ESOS HECHOS
            List<Hecho> hechos_curados = algoritmo.curarHechos(hechos);
            RepositorioHechosXColeccion.update(hechos_curados);
        }
        // por cada coleccion me fijo su algoritmo
        // busco en el repositorio de hechos por coleccion y me fijo la cantidad de veces que aparecen
        // taggeo los hechos como consensuados/curados
    }
}
