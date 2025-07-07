package domain.services;

import domain.hechos.Hecho;
import domain.colecciones.Coleccion;
import domain.colecciones.Fuente;
import domain.repositorios.RepositorioDeColecciones;
import domain.repositorios.RepositorioDeHechos;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// TODO: Analizar si tal vez conviene separar algunos metodos a un ColeccionService por ejemplo
@Service
public class HechoService {
    private final RepositorioDeColecciones repositorio_de_colecciones;
    private final RepositorioDeHechos repositorio_de_hechos;

    public HechoService(RepositorioDeColecciones repositorio_de_colecciones, RepositorioDeHechos repositorio_de_hechos) {
        this.repositorio_de_colecciones = repositorio_de_colecciones;
        this.repositorio_de_hechos = repositorio_de_hechos;
    }

    @Scheduled(fixedRate = 3600000) // Se ejecuta cada 1 hora
    public void cargarHechos() {
        System.out.println("Se ha iniciado la carga de hechos de las fuentes remotas. Esto puede tardar un rato.");
        List<Coleccion> colecciones = repositorio_de_colecciones.findAll(); // TRAE TODOS LAS COLECCIONES DEL REPOSITORIO DE COLECCIONES
        List<Fuente> fuentes = colecciones.stream().flatMap(coleccion -> coleccion.getFuentes().stream()).toList();
        List<Fuente> fuentes_sin_repetir = filtrarFuentesRepetidas(fuentes);

        for (Fuente fuente : fuentes_sin_repetir) {
            List<Hecho> hechos_recibidos = fuente.hechos(); // Maneja automaticamente las fechas y todo eso
            //TODO: repositorio_de_hechos.acaTenesTusHechosNuevos(hechosRecibidos, fuente);
            // Actualiza el repositorio con los nuevos hechos
            System.out.println("Hechos recibidos de fuente \"" + fuente.getId_externo() + "-" + fuente.getId_interno() + "\" : " + hechos_recibidos.size());
            repositorio_de_hechos.saveByFuente(hechos_recibidos, fuente);
        }
    }

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
    }

    public void guardarColeccion(Coleccion coleccion) {
        repositorio_de_colecciones.save(coleccion);
    }

    public List<Coleccion> obtenerColecciones() {
        return repositorio_de_colecciones.findAll();
    }

    public List<Hecho> obtenerHechosIrrestrictosPorColeccion(String id_coleccion) {
        return repositorio_de_hechos.findByColeccionId(id_coleccion);
    }
}

//GIGA TODO: TENEMOS UN REPOSITORIO POR CADA TABLA DEL DER. EL PROBLEMA ESTA EN COMO HACES EL SELECT Y EL JOIN SIN TENER UNA BASE DE DATOS LEVANTADA
// TODO ENTONCES HAY QUE LEVANTAR LOS REPOS LOCALMENTE, QUE SE CONECTEN POR PK, FK Y QUE HAGAMOS EL SELECT A MANO DENTRO DE MEMORIA
// TODO LOS REPOSITORIOS DEBERIAN CONOCERSE ENTRE SI, O SEA QUE EL REPOSITORIO DE HECHOS CONOZCA AL REPOSITORIO DE COLECCIONES Y ASI PODER HACER EL JOIN