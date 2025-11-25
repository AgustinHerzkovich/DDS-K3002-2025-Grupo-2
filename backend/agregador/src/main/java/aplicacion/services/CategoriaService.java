package aplicacion.services;

import aplicacion.repositories.CategoriaRepository;
import aplicacion.excepciones.CategoriaNoEncontradaException;
import org.springframework.stereotype.Service;
import aplicacion.domain.hechos.Categoria;

@Service
public class CategoriaService {
    private CategoriaRepository categoriaRepository;
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria obtenerCategoriaPorNombre(String nombre) throws CategoriaNoEncontradaException{
       return categoriaRepository.findByNombre(nombre).orElseThrow(() -> new CategoriaNoEncontradaException("No se encontró la categoría con nombre: " + nombre));
    }

    public Categoria agregarCategoria(String nombre) {
        return categoriaRepository.save(new Categoria(nombre));
    }
}
