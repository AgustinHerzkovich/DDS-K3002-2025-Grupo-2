package domain.schedullers;

import domain.colecciones.fuentes.Fuente;
import domain.hechos.Hecho;
import domain.services.FuenteService;
import domain.services.HechoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CargarHechosScheduller {
    private final FuenteService fuenteService;
    private final HechoService hechoService;

    public CargarHechosScheduller(FuenteService fuenteService, HechoService hechoService) {
        this.fuenteService = fuenteService;
        this.hechoService = hechoService;
    }

    @Scheduled(initialDelay = 180000, fixedRate = 3600000) // Se ejecuta cada 1 hora
    public void cargarHechos() {
        System.out.println("Se ha iniciado la carga de hechos de las fuentes remotas. Esto puede tardar un rato.");
        Map<Fuente, List<Hecho>> hechosPorFuente = fuenteService.hechosUltimaPeticion();
        hechoService.guardarHechos(hechosPorFuente.values().stream().flatMap(List::stream).toList()); // Carga en la tabla Hechos
        hechoService.guardarHechosPorFuente(hechosPorFuente); // Carga en la tabla HechosXFuente
        hechoService.guardarHechosPorColeccion(hechosPorFuente); // Carga en la tabla HechosXColeccion
    }
}