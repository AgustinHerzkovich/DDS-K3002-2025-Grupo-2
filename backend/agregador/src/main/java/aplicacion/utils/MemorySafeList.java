package aplicacion.utils;

import jakarta.validation.constraints.NotNull;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class MemorySafeList<T> implements Iterable<T> {

    private final List<SoftReference<T>> list = new ArrayList<>();
    private final int maxSize;

    public MemorySafeList(int maxSize) {
        if(maxSize <= 0){
            throw new RuntimeException("El tamaño de MemorySafeList no puede ser <= 0");
        }
        this.maxSize = maxSize;
    }

    public void add(T elemento) {
        if (list.size() >= maxSize) {
            list.removeFirst();
        }
        list.add(new SoftReference<>(elemento));
    }
    public void addAll(List<T> elementos){
        elementos.forEach(this::add);
    }


    public int size() {
        return toList().size();
    }
    public Stream<T> stream() {
        return list.stream()
                .map(SoftReference::get)
                .filter(Objects::nonNull);
    }
    public List<T> toList() {
        return stream().toList();
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return toList().iterator();
    }

    public String toString(){
        return toList().toString();
    }
}
