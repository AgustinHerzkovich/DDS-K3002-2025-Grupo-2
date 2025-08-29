package aplicacion.services.normalizador;

import aplicacion.repositorios.RepositorioDeEtiquetas;
import domain.hechos.Etiqueta;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtiquetaService {
    private final RepositorioDeEtiquetas repositorioDeEtiquetas;

    public EtiquetaService(RepositorioDeEtiquetas repositorioDeEtiquetas) {
        this.repositorioDeEtiquetas = repositorioDeEtiquetas;
    }

    public List<Etiqueta> obtenerEtiquetas(List<String> nombres) {
        List<Etiqueta> etiquetas;
        for (String nombre : nombres) {
            Etiqueta etiqueta = repositorioDeEtiquetas.findByNombre(nombre);
            if (etiqueta == null) {
                throw new IllegalArgumentException("Etiqueta no encontrada: " + nombre);
            }
            etiquetas.add(etiqueta);
        }

    }
}
