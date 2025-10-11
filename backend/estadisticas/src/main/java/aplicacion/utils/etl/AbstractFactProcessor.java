package aplicacion.utils.etl;


import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractFactProcessor<T, ID> {

    List<DimensionHandler<?, ?, T>> dimensionHandlers;


    //Dado un hecho de entrada (sin ID), agrupa duplicados y suma ocurrencias.
    protected Set<T> agrupar(List<T> sinAgrupar) {
        Set<T> agrupados = new HashSet<>();
        for (T nuevo : sinAgrupar) {
            if (!agrupados.add(nuevo)) {
                T existente = agrupados.stream().filter(nuevo::equals).findFirst().orElseThrow();
                sumarOcurrencias(existente, nuevo);
            }
        }
        return agrupados;
    }


    // Procesa y guarda todos los hechos
    public void procesar(List<T> hechos) {
        Set<T> agrupados = agrupar(hechos);

        resolverDimensiones(agrupados);

        for (T h : agrupados) {
            asignarId(h);
        }

        Set<ID> ids = agrupados.stream().map(this::getId).collect(Collectors.toSet());
        Map<ID, T> existentes = buscarExistentes(ids);

        List<T> hechosParaGuardar = new ArrayList<>();

        for (T nuevo : agrupados) {
            ID id = getId(nuevo);
            if (existentes.containsKey(id)) {
                T existente = existentes.get(id);
                sumarOcurrencias(existente, nuevo);
                hechosParaGuardar.add(existente);
            } else {
                hechosParaGuardar.add(nuevo);
            }
        }

        guardarTodos(hechosParaGuardar);
    }

    protected void resolverDimensiones(Set<T> hechos) {
        dimensionHandlers.forEach(h -> h.resolve(hechos));
    }

    // Métodos abstractos a implementar
    protected abstract ID getId(T h);       // ID que identifica univocamente en la consulta a la BD
    protected abstract void asignarId(T h);  // Asigna ID embedded a partir de los demas atributos
    protected abstract Map<ID, T> buscarExistentes(Set<ID> ids);    // Metodo para que el repo busque facts con la misma ID, devueltos en un map de <ID, fact>
    protected abstract void guardarTodos(List<T> hechos);   //  Metodo para que el repo guarde todos los facts
    protected abstract void sumarOcurrencias(T existente, T nuevo); // Metodo para sumar ocurrencias entre dos facts
}