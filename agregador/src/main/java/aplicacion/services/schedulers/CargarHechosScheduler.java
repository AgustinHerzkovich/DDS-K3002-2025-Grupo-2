package aplicacion.services.schedulers;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.clasesIntermedias.HechoXColeccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.services.ColeccionService;
import aplicacion.services.HechoService;
import aplicacion.services.depurador.DepuradorDeHechos;
import aplicacion.services.normalizador.NormalizadorDeHechos;
import aplicacion.services.FuenteService;
import aplicacion.utils.ProgressBar;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class CargarHechosScheduler {
    private final FuenteService fuenteService;
    private final ColeccionService coleccionService;
    private final NormalizadorDeHechos normalizadorDeHechos;
    private final DepuradorDeHechos depuradorDeHechos;
    private final HechoService hechoService;

    public CargarHechosScheduler(FuenteService fuenteService, ColeccionService coleccionService, NormalizadorDeHechos normalizadorDeHechos, DepuradorDeHechos depuradorDeHechos, HechoService hechoService) {
        this.fuenteService = fuenteService;
        this.coleccionService = coleccionService;
        this.normalizadorDeHechos = normalizadorDeHechos;
        this.depuradorDeHechos = depuradorDeHechos;
        this.hechoService = hechoService;
    }

    @Scheduled(initialDelay = 30000, fixedRate = 3600000) // Se ejecuta cada 1 hora
    @Transactional
    public void cargarHechos() {
        System.out.println("Se ha iniciado la carga de hechos de las fuentes remotas. Esto puede tardar un rato. ("+ LocalDateTime.now() + ")");
        List<Coleccion> colecciones = coleccionService.obtenerColecciones();
        for (Coleccion coleccion : colecciones) {
            System.out.println("Cargando coleccion: " + coleccion.getId() + " " + coleccion.getTitulo());
            LocalDateTime inicioCarga = LocalDateTime.now();
            List<Fuente> fuentes= coleccion.getFuentes();
            Map<Fuente, List<Hecho>> hechosPorFuente = fuenteService.hechosUltimaPeticion(fuentes);
            LocalDateTime finCarga = LocalDateTime.now();
            System.out.println("Tiempo de carga = " + Duration.between(inicioCarga, finCarga).toSeconds() + "s "+ Duration.between(inicioCarga, finCarga).toMillisPart() + "ms");
            normalizadorDeHechos.normalizarTodos(hechosPorFuente);
            LocalDateTime finNormalizacion= LocalDateTime.now();
            System.out.println("Tiempo de normalizacion = " + Duration.between(finCarga, finNormalizacion).toSeconds() + "s "+ Duration.between(finCarga, finNormalizacion).toMillisPart() + "ms");
            depuradorDeHechos.depurar(hechosPorFuente); // Depura hechos repetidos
            LocalDateTime finDepuracion = LocalDateTime.now();
            System.out.println("Tiempo de depuracion = " + Duration.between(finNormalizacion, finDepuracion).toSeconds() + "s "+ Duration.between(finNormalizacion, finDepuracion).toMillisPart() + "ms");
        }
        System.out.println("Se asignaran los hechos a las colecciones...");
        Long inicioAsignacion = System.nanoTime();
        int indiceColeccion = 0;
        int indiceFuente = 0;
        for(Coleccion coleccion : colecciones){
            indiceColeccion++;
            System.out.println("Coleccion: " + indiceColeccion + " / " + colecciones.size());

            for(Fuente fuente : coleccion.getFuentes()){
                indiceFuente++;
                List<Hecho> hechosObtenidos = fuente.getHechos();
                ProgressBar progressBar = new ProgressBar(hechosObtenidos.size(), "Fuente: "+indiceFuente+" / " + coleccion.getFuentes().size());
                // hechosObtenidos = hechosObtenidos.stream.filter(hecho->hecho.noEstaPresente).toList();
                for (Hecho hecho : hechosObtenidos) {
                    HechoXColeccion hechoPorColeccion = new HechoXColeccion(hecho, coleccion);
                    hechoService.guardarHechoPorColeccion(hechoPorColeccion);
                    progressBar.avanzar();
                }
            }
            indiceFuente = 0;
        }
        Long finAsignacion = System.nanoTime();
        System.out.printf("Se asignaron los hechos a las colecciones ( %2d ms )\n", (finAsignacion - inicioAsignacion)/1_000_000);

        // abrir map de fuente y lista de hechos, por cada fuente (fuente1, fuente2, ...) cargamos los hechos
        // for (fuente) {fuente.hechos.cargarHechos()} en el metodo que haga esa carga de hechos se hace la validacion de si el hecho ya existe en bd (mediante equals)
        // si ya existe no se carga el hecho pero se carga una entrada en HechoXFuente que asocie esta fuente y el hecho que ya existia
        // si no existe se carga el hecho y se carga la entrada en HechoXFuente
        // si se duplica un hecho dentro de la misma fuente -> Se sobreescribe? Presuponemos que no hay hechos duplicados dentro de una misma fuente
        // en ambos casos se carga la entrada en hechoxfuente, lo que varia es a que hecho apunta.
        // DECISION DE DISEÑO: si un hecho esta duplicado, conservamos el que estaba antes en la base de datos y descartamos el nuevo.
        System.out.println("Carga de hechos finalizada.");
    }

}