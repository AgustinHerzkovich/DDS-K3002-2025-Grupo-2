package aplicacion.services.depurador;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.domain.colecciones.HechoXColeccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.HechoXFuente;
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

    public void depurar(Coleccion coleccion, Map<Fuente, List<Hecho>> hechosPorFuente) {
        for (Map.Entry<Fuente, List<Hecho>> entry: hechosPorFuente.entrySet()) {
            Fuente fuente = entry.getKey();
            List<Hecho> hechos = entry.getValue();
            HechoXFuente hechoPorFuente;
            HechoXColeccion hechoPorColeccion;

            for (Hecho hecho : hechos) {
                try { // Si el hecho está duplicado en BD mediante ciertos atributos, se obtiene el hecho existente, y se asocia a la fuente
                    Hecho hechoExistente = hechoService.obtenerDuplicado(hecho);
                    hechoPorFuente = new HechoXFuente(hechoExistente, fuente);
                    hechoPorColeccion = new HechoXColeccion(hechoExistente, coleccion);
                } catch (HechoNoEncontradoException e) { // Si el hecho no está duplicado en BD mediante ciertos atributos, se guarda el hecho nuevo y se asocia a la fuente
                    hechoService.guardarHecho(hecho);
                    hechoPorFuente = new HechoXFuente(hecho, fuente);
                    hechoPorColeccion = new HechoXColeccion(hecho, coleccion);
                }
                // En ambos casos, se guarda hechoPorFuente y hechoPorColeccion
                hechoService.guardarHechoPorFuente(hechoPorFuente);
                hechoService.guardarHechoPorColeccion(hechoPorColeccion);
            }
        }
    }
}
