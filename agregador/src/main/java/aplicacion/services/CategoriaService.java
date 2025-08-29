package aplicacion.services;

import aplicacion.repositorios.RepositorioDeCategorias;
import aplicacion.services.excepciones.CategoriaNoEncontradaException;
import org.springframework.stereotype.Service;
import aplicacion.domain.hechos.Categoria;

@Service
public class CategoriaService {
    private RepositorioDeCategorias repositorioDeCategorias;
    public CategoriaService(RepositorioDeCategorias repositorioDeCategorias) {
        this.repositorioDeCategorias = repositorioDeCategorias;
    }

    public Categoria obtenerCategoriaPorNombre(String nombre) throws CategoriaNoEncontradaException{
       return repositorioDeCategorias.findByNombre(nombre).orElseThrow(() -> new CategoriaNoEncontradaException("No se encontró la categoría con nombre: " + nombre));
    }

    public Categoria agregarCategoria(String nombre) {
        return repositorioDeCategorias.save(new Categoria(nombre));
    }
}
