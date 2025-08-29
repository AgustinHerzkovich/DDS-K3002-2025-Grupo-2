package aplicacion.services.schedulers;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.services.normalizador.NormalizadorDeHechos;
import aplicacion.services.FuenteService;
import aplicacion.services.HechoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CargarHechosScheduler {
    private final FuenteService fuenteService;
    private final HechoService hechoService;
    private final NormalizadorDeHechos normalizadorDeHechos;

    public CargarHechosScheduler(FuenteService fuenteService, HechoService hechoService, NormalizadorDeHechos normalizadorDeHechos) {
        this.fuenteService = fuenteService;
        this.hechoService = hechoService;
        this.normalizadorDeHechos = normalizadorDeHechos;
    }

    @Scheduled(initialDelay = 60000, fixedRate = 3600000) // Se ejecuta cada 1 hora
    public void cargarHechos() {
        System.out.println("Se ha iniciado la carga de hechos de las fuentes remotas. Esto puede tardar un rato.");
        Map<Fuente, List<Hecho>> hechosPorFuente = fuenteService.hechosUltimaPeticion();
        hechosPorFuente.values().stream().flatMap(List::stream).forEach(normalizadorDeHechos::normalizar); // Normaliza categoria y etiquetas
        hechosPorFuente = depurador.depurar(hechosPorFuente); // Depura hechos repetidos
        // TODO:
        // abrir map de fuente y lista de hechos, por cada fuente (fuente1, fuente2, ...) cargamos los hechos
        // for (fuente) {fuente.hechos.cargarHechos()} en el metodo que haga esa carga de hechos se hace la validacion de si el hecho ya existe en bd (mediante equals)
        // si ya existe no se carga el hecho pero se carga una entrada en HechoXFuente que asocie esta fuente y el hecho que ya existia
        // si no existe se carga el hecho y se carga la entrada en HechoXFuente
        // si se duplica un hecho dentro de la misma fuente -> Se sobreescribe? Presuponemos que no hay hechos duplicados dentro de una misma fuente
        // en ambos casos se carga la entrada en hechoxfuente, lo que varia es a que hecho apunta.
        // DECISION DE DISEÑO: si un hecho esta duplicado, conservamos el que estaba antes en la base de datos y descartamos el nuevo.
        hechoService.guardarHechos(hechosPorFuente.values().stream().flatMap(List::stream).toList()); // Carga en la tabla Hechos
        hechoService.guardarHechosPorFuente(hechosPorFuente); // Carga en la tabla HechosXFuente
        hechoService.guardarHechosPorColeccion(hechosPorFuente); // Carga en la tabla HechosXColeccion
        System.out.println("Carga de hechos finalizada.");
    }

}