package testUtils;

import aplicacion.domain.hechos.Categoria;

public class CategoriaFactory {
    public static Categoria obtenerCategoriaAleatoria() {
        return new Categoria(RandomThingsGenerator.generarPalabraAleatoria());
    }
}
