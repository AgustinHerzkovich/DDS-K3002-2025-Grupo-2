package aplicacion.services;

import aplicacion.repositorios.RepositorioDeEtiquetas;
import aplicacion.services.excepciones.EtiquetaNoEncontradaException;
import aplicacion.domain.hechos.Etiqueta;
import org.springframework.stereotype.Service;

@Service
public class EtiquetaService {
    private final RepositorioDeEtiquetas repositorioDeEtiquetas;

    public EtiquetaService(RepositorioDeEtiquetas repositorioDeEtiquetas) {
        this.repositorioDeEtiquetas = repositorioDeEtiquetas;
    }

    public Etiqueta obtenerEtiquetaPorNombre(String nombre) throws EtiquetaNoEncontradaException {
        return repositorioDeEtiquetas.findByNombre(nombre).orElseThrow(() -> new EtiquetaNoEncontradaException("Etiqueta no encontrada con nombre: " + nombre));
    }

    public Etiqueta agregarEtiqueta(String nombre) {
        return repositorioDeEtiquetas.save(new Etiqueta(nombre));
    }
}
