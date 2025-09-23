package aplicacion.services.depurador;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.services.FuenteService;
import aplicacion.services.HechoService;
import aplicacion.excepciones.HechoNoEncontradoException;
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

            long inicioFuente = System.nanoTime();

            List<Hecho> hechosDuplicadosDeBD = hechoService.hallarHechosDuplicadosDeBD(hechos);


            // Saca los que ya estan en la BD de lista "hechos"
            hechoService.quitarHechosSegunCodigoUnico(hechos, hechosDuplicadosDeBD);

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
            System.out.println("        Fin depuración de la fuente: " + fuente.getId() + ". Tiempo: " + tiempoTotal /1_000_000 + " ms.  " + + (hechos.size() / (tiempoTotal/1_000_000_000.0)) + " hechos/segundo." );
            }catch (Exception ignored){
                System.out.println("Fin depuracion");
            }
            }
    }
}
