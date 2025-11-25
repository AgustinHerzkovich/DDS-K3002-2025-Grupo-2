package aplicacion.services;

import aplicacion.repositories.EtiquetaRepository;
import aplicacion.excepciones.EtiquetaNoEncontradaException;
import aplicacion.domain.hechos.Etiqueta;
import org.springframework.stereotype.Service;

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
}
