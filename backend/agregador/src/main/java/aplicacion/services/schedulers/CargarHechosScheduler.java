package aplicacion.services.schedulers;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.clasesIntermedias.HechoXColeccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.services.*;
import aplicacion.services.depurador.DepuradorDeHechos;
import aplicacion.services.normalizador.NormalizadorDeHechos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CargarHechosScheduler {
    private final FuenteService fuenteService;
    private final ColeccionService coleccionService;
    private final NormalizadorDeHechos normalizadorDeHechos;
    private final DepuradorDeHechos depuradorDeHechos;
    private final HechoService hechoService;
    private final FuenteMutexManager fuenteMutexManager;
    private final Logger logger = LoggerFactory.getLogger(CargarHechosScheduler.class);
    @Value("${hechos.lazy-loading}")
    boolean hechosSeCarganSoloSiEstanEnUnaColeccion;
    public CargarHechosScheduler(FuenteService fuenteService, ColeccionService coleccionService, NormalizadorDeHechos normalizadorDeHechos, DepuradorDeHechos depuradorDeHechos, HechoService hechoService, FuenteMutexManager fuenteMutexManager) {
        this.fuenteService = fuenteService;
        this.coleccionService = coleccionService;
        this.normalizadorDeHechos = normalizadorDeHechos;
        this.depuradorDeHechos = depuradorDeHechos;
        this.hechoService = hechoService;
        this.fuenteMutexManager = fuenteMutexManager;
    }

    @Scheduled(initialDelay = 30000, fixedRate = 3600000) // Se ejecuta cada 1 hora
    public void scheduledCargarHechos(){
        cargarHechos();
    }

    @Transactional
    public void cargarHechos() {

        logger.info("Carga de hechos de fuentes remotas iniciada");
        long inicioInicial = System.nanoTime();


        List<Coleccion> colecciones = coleccionService.obtenerColecciones();
        Set<Fuente> fuenteSet = new HashSet<>();
        if(hechosSeCarganSoloSiEstanEnUnaColeccion){
            for (Coleccion coleccion : colecciones){
                fuenteSet.addAll(coleccion.getFuentes());
            }
        }else{
            fuenteSet.addAll(fuenteService.obtenerTodasLasFuentes());
        }


        Set<String> locks = fuenteSet.stream().map(Fuente::getId).collect(Collectors.toSet());
        fuenteMutexManager.lockAll(locks);
        try {

            logger.info("Se normalizaran {} fuentes", fuenteSet.size());

        Map<Fuente, List<Hecho>> hechosPorFuente = fuenteService.hechosUltimaPeticion(fuenteSet);
        normalizadorDeHechos.normalizarTodos(hechosPorFuente);
        depuradorDeHechos.depurar(hechosPorFuente); // Depura hechos repetidos

            logger.info("Carga de hechos finalizada. Asociando hechos a colecciones");


        Long inicioAsignacion = System.nanoTime();
        int indiceColeccion = 0;
        int indiceFuente = 0;
        for(Coleccion coleccion : colecciones){
            indiceColeccion++;
            logger.debug("Asignando hechos de colección {} : {} / {}", coleccion.getId(), indiceColeccion, colecciones.size());
            for(Fuente fuente : coleccion.getFuentes()){
                indiceFuente++;
                List<Hecho> hechosObtenidos = hechosPorFuente.get(fuente);
                if (hechosObtenidos == null) {
                    logger.warn("   ⚠ Fuente {} ({}): No devolvió hechos o falló la conexión. Saltando...", indiceFuente, fuente.getAlias());
                    continue;
                }
                // hechosObtenidos = hechosObtenidos.stream.filter(hecho->hecho.noEstaPresente).toList();
                for (Hecho hecho : hechosObtenidos) {
                    HechoXColeccion hechoPorColeccion = new HechoXColeccion(hecho, coleccion);
                    hechoService.guardarHechoPorColeccion(hechoPorColeccion);
                }
            }
        indiceFuente = 0;
        }
        Long finAsignacion = System.nanoTime();
        logger.info("Se asignaron los hechos a las colecciones {} ", String.format("( %2d ms )", (finAsignacion - inicioAsignacion)/1_000_000));
            // abrir map de fuente y lista de hechos, por cada fuente (fuente1, fuente2, ...) cargamos los hechos
            // for (fuente) {fuente.hechos.cargarHechos()} en el metodo que haga esa carga de hechos se hace la validacion de si el hecho ya existe en bd (mediante equals)
            // si ya existe no se carga el hecho pero se carga una entrada en HechoXFuente que asocie esta fuente y el hecho que ya existia
            // si no existe se carga el hecho y se carga la entrada en HechoXFuente
            // si se duplica un hecho dentro de la misma fuente -> Se sobreescribe? Presuponemos que no hay hechos duplicados dentro de una misma fuente
            // en ambos casos se carga la entrada en hechoxfuente, lo que varia es a que hecho apunta.
            // DECISION DE DISEÑO: si un hecho esta duplicado, conservamos el que estaba antes en la base de datos y descartamos el nuevo.
        }catch (Exception e){
            logger.error("No se pudo concretar la asignacion de hechos", e);
        }finally {
            fuenteMutexManager.unlockAll(locks);
        }
        logger.info("Carga de hechos de fuentes remotas finalizada. ({}ms)", (System.nanoTime() - inicioInicial)/1_000_000);
    }

}