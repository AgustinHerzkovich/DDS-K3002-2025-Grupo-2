package aplicacion.services.schedulers;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.FuenteXColeccion;
import aplicacion.domain.hechos.Hecho;
import aplicacion.services.ColeccionService;
import aplicacion.services.depurador.DepuradorDeHechos;
import aplicacion.services.normalizador.NormalizadorDeHechos;
import aplicacion.services.FuenteService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CargarHechosScheduler {
    private final FuenteService fuenteService;
    private final ColeccionService coleccionService;
    private final NormalizadorDeHechos normalizadorDeHechos;
    private final DepuradorDeHechos depuradorDeHechos;

    public CargarHechosScheduler(FuenteService fuenteService, ColeccionService coleccionService, NormalizadorDeHechos normalizadorDeHechos, DepuradorDeHechos depuradorDeHechos) {
        this.fuenteService = fuenteService;
        this.coleccionService = coleccionService;
        this.normalizadorDeHechos = normalizadorDeHechos;
        this.depuradorDeHechos = depuradorDeHechos;
    }

    @Scheduled(initialDelay = 20000, fixedRate = 3600000) // Se ejecuta cada 1 hora
    public void cargarHechos() {
        System.out.println("Se ha iniciado la carga de hechos de las fuentes remotas. Esto puede tardar un rato. ("+ LocalDateTime.now() + ")");
        List<Coleccion> colecciones = coleccionService.obtenerColecciones();
        for (Coleccion coleccion : colecciones) {
            List<FuenteXColeccion> fuentesPorColeccion = coleccion.getFuentes().stream().map(fuente -> new FuenteXColeccion(fuente, coleccion)).toList();
            Map<Fuente, List<Hecho>> hechosPorFuente = fuenteService.hechosUltimaPeticion(fuentesPorColeccion);
            System.out.println("Inicio normalizacion: " + LocalDateTime.now());
            normalizadorDeHechos.normalizarMultiThread(hechosPorFuente);
            System.out.println("Fin normalizacion e inicio depuracion: " + LocalDateTime.now());
            depuradorDeHechos.depurar(coleccion, hechosPorFuente); // Depura hechos repetidos
        }   System.out.println("Fin depuracion: " + LocalDateTime.now());
        // abrir map de fuente y lista de hechos, por cada fuente (fuente1, fuente2, ...) cargamos los hechos
        // for (fuente) {fuente.hechos.cargarHechos()} en el metodo que haga esa carga de hechos se hace la validacion de si el hecho ya existe en bd (mediante equals)
        // si ya existe no se carga el hecho pero se carga una entrada en HechoXFuente que asocie esta fuente y el hecho que ya existia
        // si no existe se carga el hecho y se carga la entrada en HechoXFuente
        // si se duplica un hecho dentro de la misma fuente -> Se sobreescribe? Presuponemos que no hay hechos duplicados dentro de una misma fuente
        // en ambos casos se carga la entrada en hechoxfuente, lo que varia es a que hecho apunta.
        // DECISION DE DISEÑO: si un hecho esta duplicado, conservamos el que estaba antes en la base de datos y descartamos el nuevo.
        System.out.println("Carga de hechos finalizada.");
    }

}