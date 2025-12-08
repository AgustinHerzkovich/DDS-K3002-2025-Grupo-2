package aplicacion.services;

import aplicacion.clasesIntermedias.HechoXColeccion;
import aplicacion.domain.colecciones.Coleccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.events.ColeccionCreadaEvent;
import aplicacion.events.FuenteAgregadaAColeccionEvent;
import aplicacion.excepciones.ColeccionNoEncontradaException;
import aplicacion.repositories.ColeccionRepository;
import aplicacion.repositories.HechoXColeccionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ColeccionAsyncService {
    private final FuenteService fuenteService;
    private final HechoXColeccionRepository hechoXColeccionRepository;
    private final ColeccionRepository coleccionRepository;
    private final FuenteMutexManager fuenteMutexManager;
    private final Logger logger = LoggerFactory.getLogger(ColeccionAsyncService.class);

    public ColeccionAsyncService(FuenteService fuenteService, HechoXColeccionRepository hechoXColeccionRepository, ColeccionRepository coleccionRepository, FuenteMutexManager fuenteMutexManager) {
        this.fuenteService = fuenteService;
        this.hechoXColeccionRepository = hechoXColeccionRepository;
        this.coleccionRepository = coleccionRepository;
        this.fuenteMutexManager = fuenteMutexManager;
    }

    /**
     * Listener que se ejecuta DESPUÉS del commit cuando se crea una colección.
     * Este metodo recibe el evento y delega al metodo async.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void onColeccionCreada(ColeccionCreadaEvent event) {
        logger.debug("Evento recibido: Colección creada con ID: {}", event.getColeccionId());
        fuenteMutexManager.lockAll(new HashSet<>(event.getFuentesId()));
        logger.debug("({})  -  Insercion de hechos iniciada", event.getColeccionId());
        try {
            asociarHechosPreexistentes(event.getColeccionId());
        }catch (Exception e){
            logger.error("({})  -  Error: No se pudo cargar los hechos de una de las fuentes   -   {}", event.getColeccionId(), e.getMessage());
        }finally {
            fuenteMutexManager.unlockAll(new HashSet<>(event.getFuentesId()));
            logger.debug("({})  -  Insercion de hechos finalizada exitosamente", event.getColeccionId());
        }
    }

    /**
     * Listener que se ejecuta DESPUÉS del commit cuando se agrega una fuente a una colección.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void onFuenteAgregada(FuenteAgregadaAColeccionEvent event) {
        logger.debug("Evento recibido: Fuente agregada - Colección: {}, Fuente: {}", event.getColeccionId(), event.getFuenteId());
        fuenteMutexManager.lock(event.getFuenteId());
        logger.debug("({})  -  Insercion de hechos iniciada", event.getColeccionId());
        try {
            // Recargar la colección en una nueva transacción
            Coleccion coleccion = coleccionRepository.findById(event.getColeccionId())
                    .orElseThrow(() -> new ColeccionNoEncontradaException("Colección no encontrada con ID: " + event.getColeccionId()));
            asociarHechosPreexistentesDeFuenteAColeccion(coleccion, event.getFuenteId());
        }catch (Exception e){
            logger.error("({})  -  Error: no se pudo cargar los hechos de la fuente {}  -   {}", event.getColeccionId(), event.getFuenteId(), e.getMessage());
        }
        finally {
            fuenteMutexManager.unlock(event.getFuenteId());
            logger.debug("({})  -  Insercion de hechos finalizada exitosamente", event.getColeccionId());
        }

    }

    @Transactional
    public void asociarHechosPreexistentes(String coleccionId) {
        // Recargar la colección en una nueva transacción dentro del hilo asíncrono
        Coleccion coleccion = coleccionRepository.findById(coleccionId)
                .orElseThrow(() -> new ColeccionNoEncontradaException("Colección no encontrada con ID: " + coleccionId));

        logger.info("({})  -  Asociando hechos preexistentes de {} {} con {} fuentes", coleccionId, coleccion.getId(), coleccion.getTitulo(), coleccion.getFuentes().size());
        List<Fuente> fuentes = coleccion.getFuentes();
        for (Fuente fuente : fuentes) {
            asociarHechosPreexistentesDeFuenteAColeccion(coleccion, fuente.getId());
        }
    }

    @Transactional
    public void asociarHechosPreexistentesDeFuenteAColeccion(Coleccion coleccion, String fuenteId){
        logger.info("({})  -  Asociando {} {} con fuente {}", coleccion.getId(), coleccion.getId(), coleccion.getTitulo(), fuenteId);
        List<Hecho> hechosDeFuente = fuenteService.obtenerHechosPorFuente(fuenteId);
        List<Hecho> hechosQueCumplenCriterios = hechosDeFuente.stream()
                .filter(coleccion::cumpleCriterios)
                .toList();
        List<HechoXColeccion> hechosXColeccion = hechosQueCumplenCriterios.stream()
                .map(hecho -> new HechoXColeccion(hecho, coleccion))
                .collect(Collectors.toList());
        hechoXColeccionRepository.saveAll(hechosXColeccion);
    }
}
