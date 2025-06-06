package repositorios;

import java.util.List;

public interface Repositorio<T> {
    void agregar(T objeto);

    void quitar(T objeto);

    T buscar(T objeto);

    List<T> listar();
}
