package aplicacion.services.normalizador;

import aplicacion.repositorios.RepositorioDeCategorias;
import org.springframework.stereotype.Service;
import domain.hechos.Categoria;

@Service
public class CategoriaService {
    private RepositorioDeCategorias categoriaRepository;
    public CategoriaService(RepositorioDeCategorias categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria obtenerCategoria(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }




}
