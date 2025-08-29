package aplicacion.services;

import aplicacion.repositorios.RepositorioDeContribuyentes;
import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import org.springframework.stereotype.Service;

@Service
public class ContribuyenteService {
    private final RepositorioDeContribuyentes repositorioDeContribuyentes;

    public ContribuyenteService(RepositorioDeContribuyentes repositorioDeContribuyentes) {
        this.repositorioDeContribuyentes = repositorioDeContribuyentes;
    }

    public Contribuyente guardarContribuyente(Contribuyente contribuyente) {
        return repositorioDeContribuyentes.save(contribuyente);
    }

    public Contribuyente obtenerContribuyente(Long id) {
        return repositorioDeContribuyentes.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contribuyente no encontrado con ID: " + id));
    }

    public IdentidadContribuyente obtenerIdentidad(Long id) {
        return repositorioDeContribuyentes.findIdentidadById(id)
                .orElseThrow(() -> new IllegalArgumentException("Identidad no encontrada para el contribuyente con ID: " + id));
    }

    public Contribuyente agregarIdentidadAContribuyente(Long id, IdentidadContribuyente identidad) {
        Contribuyente contribuyente = obtenerContribuyente(id);
        contribuyente.agregarIdentidad(identidad);
        return repositorioDeContribuyentes.save(contribuyente);
    }
}