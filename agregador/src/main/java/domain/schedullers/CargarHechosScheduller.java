package domain.schedullers;

import domain.colecciones.fuentes.Fuente;
import domain.hechos.Hecho;
import domain.repositorios.RepositorioDeFuentes;
import domain.repositorios.RepositorioDeHechos;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CargarHechosScheduller {
    private final RepositorioDeHechos repositorioDeHechos;
    private final RepositorioDeFuentes repositorioDeFuentes;

    public CargarHechosScheduller(RepositorioDeHechos repositorioDeHechos, RepositorioDeFuentes repositorioDeFuentes) {
        this.repositorioDeHechos = repositorioDeHechos;
        this.repositorioDeFuentes = repositorioDeFuentes;
    }

    @Scheduled(fixedRate = 3600000) // Se ejecuta cada 1 hora
    public void cargarHechos() {
        System.out.println("Se ha iniciado la carga de hechos de las fuentes remotas. Esto puede tardar un rato.");
        //List<Coleccion> colecciones = repositorio_de_colecciones.findAll(); // TRAE TODOS LAS COLECCIONES DEL REPOSITORIO DE COLECCIONES
        //List<Fuente> fuentes = colecciones.stream().flatMap(coleccion -> coleccion.getFuentes().stream()).toList();
        //List<Fuente> fuentes_sin_repetir = filtrarFuentesRepetidas(fuentes);
        List<Fuente> fuentes = repositorioDeFuentes.findAll();

        for (Fuente fuente : fuentes) {
            List<Hecho> hechosRecibidos = fuente.hechos(); // Maneja automaticamente las fechas y todo eso
            //TODO: repositorio_de_hechos.acaTenesTusHechosNuevos(hechosRecibidos, fuente);
            // Actualiza el repositorio con los nuevos hechos
            //System.out.println("Hechos recibidos de fuente \"" + fuente.getId_externo() + "-" + fuente.getId_interno() + "\" : " + hechosRecibidos.size());
            repositorioDeHechos.saveByFuente(hechosRecibidos, fuente);
        }
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
