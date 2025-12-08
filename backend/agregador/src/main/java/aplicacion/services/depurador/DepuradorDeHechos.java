package aplicacion.services.depurador;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.services.FuenteMutexManager;
import aplicacion.services.FuenteService;
import aplicacion.services.HechoService;
import aplicacion.utils.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DepuradorDeHechos {
    private final HechoService hechoService;
    private final FuenteService fuenteService;
    private final Logger logger = LoggerFactory.getLogger(DepuradorDeHechos.class);

    public DepuradorDeHechos(HechoService hechoService, FuenteService fuenteService) {
        this.hechoService = hechoService;
        this.fuenteService = fuenteService;
    }

    public void depurar(Map<Fuente, List<Hecho>> hechosPorFuente) {
        logger.info("Depuracion iniciada con {} fuentes", hechosPorFuente.size());
        for (Map.Entry<Fuente, List<Hecho>> entry: hechosPorFuente.entrySet()) {
            Fuente fuente = entry.getKey();
            logger.info("Depurando hechos de la fuente: {} con {} hechos.", fuente.getId(), entry.getValue().size());
            List<Hecho> hechos = new ArrayList<>(entry.getValue());


            long inicioFuente = System.nanoTime();
            List<Hecho> hechosDuplicadosDeBD = hechoService.hallarHechosDuplicadosDeBD(hechos);
            List<Hecho> hechosDuplicadosDeLista = hechoService.hallarHechosDuplicadosDeLista(hechos);



            // Saca los que ya estan en la BD de lista "hechos"
            hechoService.quitarHechosDeSublista(hechos, hechosDuplicadosDeBD);
            hechoService.quitarHechosDeSublista(hechos, hechosDuplicadosDeLista);
            hechos.addAll(hechosDuplicadosDeLista); // Los agrega 1 vez para que estén.

            // No guarda los repetidos
            hechoService.guardarHechos(hechos);

            // Usa los que se van a crear
            fuente.agregarHechos(hechos);

            // Usa los de la BD
            fuente.agregarHechos(hechosDuplicadosDeBD);


            fuenteService.guardarFuente(fuente);

            long finFuente = System.nanoTime();
            // Cálculo de tiempos
            long tiempoTotal = finFuente - inicioFuente;

            try {
                logger.debug("Fin depuración de la fuente: {}. Tiempo: {} ms.  {} hechos/segundo.", fuente.getId(), tiempoTotal / 1_000_000, +(hechos.size() / (tiempoTotal / 1_000_000_000.0)));
            }catch (Exception ignored){
                logger.debug("Fin depuracion");
            }
            }
    }
}
