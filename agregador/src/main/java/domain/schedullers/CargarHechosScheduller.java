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
    private final RepositorioDeHechos repositorio_de_hechos;
    private final RepositorioDeFuentes repositorio_de_fuentes;

    public CargarHechosScheduller(RepositorioDeHechos repositorio_de_hechos, RepositorioDeFuentes repositorio_de_fuentes) {
        this.repositorio_de_hechos = repositorio_de_hechos;
        this.repositorio_de_fuentes = repositorio_de_fuentes;
    }

    @Scheduled(fixedRate = 3600000) // Se ejecuta cada 1 hora
    public void cargarHechos() {
        System.out.println("Se ha iniciado la carga de hechos de las fuentes remotas. Esto puede tardar un rato.");
        //List<Coleccion> colecciones = repositorio_de_colecciones.findAll(); // TRAE TODOS LAS COLECCIONES DEL REPOSITORIO DE COLECCIONES
        //List<Fuente> fuentes = colecciones.stream().flatMap(coleccion -> coleccion.getFuentes().stream()).toList();
        //List<Fuente> fuentes_sin_repetir = filtrarFuentesRepetidas(fuentes);
        List<Fuente> fuentes = repositorio_de_fuentes.findAll();

        for (Fuente fuente : fuentes) {
            List<Hecho> hechos_recibidos = fuente.hechos(); // Maneja automaticamente las fechas y todo eso
            //TODO: repositorio_de_hechos.acaTenesTusHechosNuevos(hechosRecibidos, fuente);
            // Actualiza el repositorio con los nuevos hechos
            //System.out.println("Hechos recibidos de fuente \"" + fuente.getId_externo() + "-" + fuente.getId_interno() + "\" : " + hechos_recibidos.size());
            repositorio_de_hechos.saveByFuente(hechos_recibidos, fuente);
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
