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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColeccionAsyncService {
    private final FuenteService fuenteService;
    private final HechoXColeccionRepository hechoXColeccionRepository;
    private final ColeccionRepository coleccionRepository;

    public ColeccionAsyncService(FuenteService fuenteService, HechoXColeccionRepository hechoXColeccionRepository, ColeccionRepository coleccionRepository) {
        this.fuenteService = fuenteService;
        this.hechoXColeccionRepository = hechoXColeccionRepository;
        this.coleccionRepository = coleccionRepository;
    }

    /**
     * Listener que se ejecuta DESPUÉS del commit cuando se crea una colección.
     * Este metodo recibe el evento y delega al metodo async.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void onColeccionCreada(ColeccionCreadaEvent event) {
        System.out.println("Evento recibido: Colección creada con ID: " + event.getColeccionId());
        asociarHechosPreexistentes(event.getColeccionId());
    }

    /**
     * Listener que se ejecuta DESPUÉS del commit cuando se agrega una fuente a una colección.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void onFuenteAgregada(FuenteAgregadaAColeccionEvent event) {
        System.out.println("Evento recibido: Fuente agregada - Colección: " + event.getColeccionId() + ", Fuente: " + event.getFuenteId());

        // Recargar la colección en una nueva transacción
        Coleccion coleccion = coleccionRepository.findById(event.getColeccionId())
                .orElseThrow(() -> new ColeccionNoEncontradaException("Colección no encontrada con ID: " + event.getColeccionId()));

        asociarHechosPreexistentesDeFuenteAColeccion(coleccion, event.getFuenteId());
    }

    @Transactional
    public void asociarHechosPreexistentes(String coleccionId) {
        // Recargar la colección en una nueva transacción dentro del hilo asíncrono
        Coleccion coleccion = coleccionRepository.findById(coleccionId)
                .orElseThrow(() -> new ColeccionNoEncontradaException("Colección no encontrada con ID: " + coleccionId));

        System.out.println("Asociando hechos preexistentes de " + coleccion.getId() + " " + coleccion.getTitulo() + " con " + coleccion.getFuentes().size() + " fuentes");
        List<Fuente> fuentes = coleccion.getFuentes();
        for (Fuente fuente : fuentes) {
            asociarHechosPreexistentesDeFuenteAColeccion(coleccion, fuente.getId());
        }
    }

    @Transactional
    public void asociarHechosPreexistentesDeFuenteAColeccion(Coleccion coleccion, String fuenteId){
        System.out.println("Asociando " + coleccion.getId() + " " + coleccion.getTitulo() + " con fuente " + fuenteId);
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
