package aplicacion.services.schedullers;

import aplicacion.domain.colecciones.fuentes.Fuente;
import domain.hechos.Hecho;
import aplicacion.domain.normalizador.NormalizadorDeHechos;
import aplicacion.services.FuenteService;
import aplicacion.services.HechoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CargarHechosScheduller {
    private final FuenteService fuenteService;
    private final HechoService hechoService;
    private final NormalizadorDeHechos normalizadorDeHechos;

    public CargarHechosScheduller(FuenteService fuenteService, HechoService hechoService, NormalizadorDeHechos normalizadorDeHechos) {
        this.fuenteService = fuenteService;
        this.hechoService = hechoService;
        this.normalizadorDeHechos = normalizadorDeHechos;
    }

    @Scheduled(initialDelay = 60000, fixedRate = 3600000) // Se ejecuta cada 1 hora
    public void cargarHechos() {
        System.out.println("Se ha iniciado la carga de hechos de las fuentes remotas. Esto puede tardar un rato.");
        Map<Fuente, List<Hecho>> hechosPorFuente = fuenteService.hechosUltimaPeticion();
        hechoService.guardarHechos(hechosPorFuente.values().stream().flatMap(List::stream).map(normalizadorDeHechos::normalizar).toList()); // Carga en la tabla Hechos
        hechoService.guardarHechosPorFuente(hechosPorFuente); // Carga en la tabla HechosXFuente
        hechoService.guardarHechosPorColeccion(hechosPorFuente); // Carga en la tabla HechosXColeccion
        System.out.println("Carga de hechos finalizada.");
    }
}