package aplicacion.utils.etl;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DimensionHandler<T, K, F> {
    private final Function<T, K> getClave; // Funcion que saca la clave de la dimension
    private final Function<Set<K>, List<T>> obtenerFactsDeClaves;  // Dada un set de claves, me devuelve los objetos con esas claves ya persistidos llamando al repositorio
    private final Function<Collection<T>, List<T>> guardarDimension;  // Dada una lista de dimensiones, esta funcion las guarda en la base de datos
    private final BiConsumer<F, T> dimensionSetter;    // Funcion que dado el fact y la dimension, se la asigna
    private final Function<F, T> dimensionGetter;      // Funcion que dado el fact, devuelve la categoria

    private final Map<K, T> dimensionMap = new HashMap<>();

    public DimensionHandler(
            Function<T, K> getClave,
            Function<Set<K>, List<T>> obtenerFactsDeClaves,
            Function<Collection<T>, List<T>> guardarDimension,
            Function<F, T> dimensionGetter,
            BiConsumer<F, T> dimensionSetter
    ) {
        this.getClave = getClave;
        this.obtenerFactsDeClaves = obtenerFactsDeClaves;
        this.guardarDimension = guardarDimension;
        this.dimensionGetter = dimensionGetter;
        this.dimensionSetter = dimensionSetter;
    }

    public void resolve(Set<F> facts) {
        // Obtener claves de los hechos
        Set<K> keys = facts.stream()
                .map(f -> getClave.apply(dimensionGetter.apply(f)))
                .collect(Collectors.toSet());

        // Buscar dimensiones existentes en DB
        List<T> existentes = obtenerFactsDeClaves.apply(keys);
        Map<K, T> existentesMap = existentes.stream()
                .collect(Collectors.toMap(getClave, Function.identity()));
        dimensionMap.putAll(existentesMap);

        // Detectar claves faltantes
        Set<K> faltantes = new HashSet<>(keys);
        faltantes.removeAll(existentesMap.keySet());

        // Crear nuevas instancias de dimension para guardar
        List<T> nuevas = facts.stream()
                .map(dimensionGetter)
                .filter(dim -> faltantes.contains(getClave.apply(dim)))
                .distinct()
                .toList();

        //  Guardar en DB las nuevas instancias de la dimension
        List<T> guardadas = guardarDimension.apply(nuevas);

        // Actualizar map
        for (T nueva : guardadas) {
            dimensionMap.put(getClave.apply(nueva), nueva);
        }

        // Reasignar dimension a los hechos
        for (F f : facts) {
            K key = getClave.apply(dimensionGetter.apply(f));
            T persistida = dimensionMap.get(key);
            dimensionSetter.accept(f, persistida);
        }
    }
}