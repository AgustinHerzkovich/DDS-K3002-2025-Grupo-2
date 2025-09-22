package aplicacion.services.depurador;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.clasesIntermedias.HechoXFuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.services.HechoService;
import aplicacion.excepciones.HechoNoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DepuradorDeHechos {
    private final HechoService hechoService;

    public DepuradorDeHechos(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    public void depurar(Map<Fuente, List<Hecho>> hechosPorFuente) {
        for (Map.Entry<Fuente, List<Hecho>> entry: hechosPorFuente.entrySet()) {
            Fuente fuente = entry.getKey();
            System.out.println("Depurando hechos de la fuente: " + fuente.getId() + " con " + entry.getValue().size() + " hechos.");
            List<Hecho> hechos = entry.getValue();
            HechoXFuente hechoPorFuente;
            long inicioFuente = System.nanoTime();
            int totalHechos = hechos.size();
            int hechosProcesados = 0;
            int largoBarra = 50;
            for (Hecho hecho : hechos) {
                try { // Si el hecho está duplicado en BD mediante ciertos atributos, se obtiene el hecho existente, y se asocia a la fuente
                    Hecho hechoExistente = hechoService.obtenerDuplicado(hecho);
                    hechoPorFuente = new HechoXFuente(hechoExistente, fuente);
                } catch (HechoNoEncontradoException e) { // Si el hecho no está duplicado en BD mediante ciertos atributos, se guarda el hecho nuevo y se asocia a la fuente
                    hechoService.guardarHecho(hecho);
                    hechoPorFuente = new HechoXFuente(hecho, fuente);
                }
                hechosProcesados++;
                int porcentaje = (hechosProcesados * 100) / totalHechos;
                int llenos = (porcentaje * largoBarra) / 100;
                int vacios = largoBarra - llenos;

                String barra = "[" + "#".repeat(llenos) + "-".repeat(vacios) + "] " + porcentaje + "%" + " (" + hechosProcesados + "/" + totalHechos + ")";
                System.out.print("\r" + barra);
                // En ambos casos, se guarda hechoPorFuente
                hechoService.guardarHechoPorFuente(hechoPorFuente);
            }
            long finFuente = System.nanoTime();
            System.out.println("Fin depuración de la fuente: " + fuente.getId() + ". Tiempo: " + (finFuente - inicioFuente)/1_000_000 + " ms.");
            try {
                System.out.println("    Tasa de depuración: " + (hechos.size() / ((finFuente - inicioFuente)/1_000_000_000.0)) + " hechos/segundo.");
            }catch (Exception ignored){}
        }
    }
}
