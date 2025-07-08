package domain.schedullers;

import domain.colecciones.fuentes.Fuente;
import domain.hechos.Hecho;
import domain.repositorios.RepositorioDeFuentes;
import domain.repositorios.RepositorioDeHechos;
import domain.services.FuenteService;
import domain.services.HechoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CargarHechosScheduller {
    private final RepositorioDeHechos repositorioDeHechos;
    private final RepositorioDeFuentes repositorioDeFuentes;
    private final FuenteService fuenteService;
    private final HechoService hechoService;

    public CargarHechosScheduller(RepositorioDeHechos repositorioDeHechos, RepositorioDeFuentes repositorioDeFuentes, FuenteService fuenteService, HechoService hechoService) {
        this.repositorioDeHechos = repositorioDeHechos;
        this.repositorioDeFuentes = repositorioDeFuentes;
        this.fuenteService = fuenteService;
        this.hechoService = hechoService;
    }

    @Scheduled(fixedRate = 3600000) // Se ejecuta cada 1 hora
    public void cargarHechos() {
        //System.out.println("Se ha iniciado la carga de hechos de las fuentes remotas. Esto puede tardar un rato.");
        //List<Coleccion> colecciones = repositorio_de_colecciones.findAll(); // TRAE TODOS LAS COLECCIONES DEL REPOSITORIO DE COLECCIONES
        //List<Fuente> fuentes = colecciones.stream().flatMap(coleccion -> coleccion.getFuentes().stream()).toList();
        //List<Fuente> fuentes_sin_repetir = filtrarFuentesRepetidas(fuentes);
        Map<Fuente, List<Hecho>> ultimosHechos = fuenteService.hechosUltimaPeticion();
        hechoService.guardarHechosPorFuente(ultimosHechos);
    }
/*
    private List<Fuente> filtrarFuentesRepetidas(List<Fuente> fuentes) {
        List<Fuente> filtrado = new ArrayList<Fuente>();
        HashSet<String> vistos = new HashSet<String>(); // Hashset reduce complejidad. Mejor que una lista
        for (Fuente fuente : fuentes) { // Agrega los no vistos a la lista Filtrado. Los vistos son ignorados
            String clave = fuente.getId_externo() + "-"+ fuente.getId_interno();
            if(!vistos.contains(clave)) {
                vistos.add(clave);
                filtrado.add(fuente);
            }
        }
        return filtrado;
    }*/
}
