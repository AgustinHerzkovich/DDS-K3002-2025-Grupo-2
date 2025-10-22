package aplicacion.services.depurador;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.FuenteDinamica;
import aplicacion.domain.hechos.Hecho;
import aplicacion.services.FuenteService;
import aplicacion.services.HechoService;
import aplicacion.excepciones.HechoNoEncontradoException;
import aplicacion.utils.ProgressBar;
import org.springframework.stereotype.Service;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DepuradorDeHechos {
    private final HechoService hechoService;
    private final FuenteService fuenteService;
    public DepuradorDeHechos(HechoService hechoService, FuenteService fuenteService) {
        this.hechoService = hechoService;
        this.fuenteService = fuenteService;
    }

    public void depurar(Map<Fuente, List<Hecho>> hechosPorFuente) {
        for (Map.Entry<Fuente, List<Hecho>> entry: hechosPorFuente.entrySet()) {
            Fuente fuente = entry.getKey();
            System.out.println("Depurando hechos de la fuente: " + fuente.getId() + " con " + entry.getValue().size() + " hechos.");
            List<Hecho> hechos = new ArrayList<>(entry.getValue());


            ProgressBar progressBar = new ProgressBar(8, " " + hechos.size() + " hechos");
            long inicioFuente = System.nanoTime();
            progressBar.avanzar();
            List<Hecho> hechosDuplicadosDeBD = hechoService.hallarHechosDuplicadosDeBD(hechos);
            progressBar.avanzar();
            List<Hecho> hechosDuplicadosDeLista = hechoService.hallarHechosDuplicadosDeLista(hechos);
            progressBar.avanzar();



            // Saca los que ya estan en la BD de lista "hechos"
            hechoService.quitarHechosDeSublista(hechos, hechosDuplicadosDeBD);
            hechoService.quitarHechosDeSublista(hechos, hechosDuplicadosDeLista);
            hechos.addAll(hechosDuplicadosDeLista); // Los agrega 1 vez para que estén.
            progressBar.avanzar();

            // No guarda los repetidos
            hechoService.guardarHechos(hechos);
            progressBar.avanzar();

            // Usa los que se van a crear
            fuente.agregarHechos(hechos);
            progressBar.avanzar();

            // Usa los de la BD
            fuente.agregarHechos(hechosDuplicadosDeBD);
            progressBar.avanzar();


            fuenteService.guardarFuente(fuente);
            progressBar.avanzar();

            long finFuente = System.nanoTime();
            // Cálculo de tiempos
            long tiempoTotal = finFuente - inicioFuente;

            try {
            System.out.println("Fin depuración de la fuente: " + fuente.getId() + ". Tiempo: " + tiempoTotal /1_000_000 + " ms.  " + + (hechos.size() / (tiempoTotal/1_000_000_000.0)) + " hechos/segundo." );
            }catch (Exception ignored){
                System.out.println("Fin depuracion");
            }
            }
    }
}
