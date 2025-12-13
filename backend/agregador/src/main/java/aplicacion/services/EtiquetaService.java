package aplicacion.services;

import aplicacion.domain.hechos.Categoria;
import aplicacion.repositories.EtiquetaRepository;
import aplicacion.excepciones.EtiquetaNoEncontradaException;
import aplicacion.domain.hechos.Etiqueta;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EtiquetaService {
    private final EtiquetaRepository etiquetaRepository;

    public EtiquetaService(EtiquetaRepository etiquetaRepository) {
        this.etiquetaRepository = etiquetaRepository;
    }

    public Etiqueta obtenerEtiquetaPorNombre(String nombre) throws EtiquetaNoEncontradaException {
        return etiquetaRepository.findByNombre(nombre).orElseThrow(() -> new EtiquetaNoEncontradaException("Etiqueta no encontrada con nombre: " + nombre));
    }

    public Etiqueta agregarEtiqueta(String nombre) {
        return etiquetaRepository.save(new Etiqueta(nombre));
    }

    public List<String> obtenerAutocompletado(String currentSearch, Integer limit) {
        List<String> opciones = currentSearch.length() >= 3 ? etiquetaRepository.findAutocompletado(currentSearch, limit) : etiquetaRepository.findAutocompletadoLike(currentSearch, limit);
        if(opciones.isEmpty() && currentSearch.length() >=3){
            return etiquetaRepository.findAutocompletadoLike(currentSearch, limit);
        }
        else return opciones;
    }
    public List<String> obtenerEtiquetas(int cantidad) {
        return etiquetaRepository.findAll(PageRequest.of(0, cantidad))
                .stream()
                .map(Etiqueta::getNombre)
                .collect(Collectors.toList());
    }
}
