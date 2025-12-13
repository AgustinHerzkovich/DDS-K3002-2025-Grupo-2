package aplicacion.services;

import aplicacion.repositories.CategoriaRepository;
import aplicacion.excepciones.CategoriaNoEncontradaException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import aplicacion.domain.hechos.Categoria;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria obtenerCategoriaPorNombre(String nombre) throws CategoriaNoEncontradaException{
       return categoriaRepository.findByNombre(nombre).orElseThrow(() -> new CategoriaNoEncontradaException("No se encontró la categoría con nombre: " + nombre));
    }

    public Categoria agregarCategoria(String nombre) {
        return categoriaRepository.save(new Categoria(nombre));
    }

    public List<String> obtenerAutocompletado(String currentSearch, Integer limit) {
        List<String> opciones = currentSearch.length() >= 3 ? categoriaRepository.findAutocompletado(currentSearch, limit) : categoriaRepository.findAutocompletadoLike(currentSearch, limit);
        if(opciones.isEmpty() && currentSearch.length() >=3){
            return categoriaRepository.findAutocompletadoLike(currentSearch, limit);
        }
        else return opciones;
    }
    public List<String> obtenerCategorias(int cantidad) {
        return categoriaRepository.findAll(PageRequest.of(0, cantidad))
                .stream()
                .map(Categoria::getNombre)
                .collect(Collectors.toList());
    }
}
