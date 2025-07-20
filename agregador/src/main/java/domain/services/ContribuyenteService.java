package domain.services;

import domain.repositorios.RepositorioDeContribuyentes;
import domain.usuarios.Contribuyente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContribuyenteService {
    private final RepositorioDeContribuyentes repositorioDeContribuyentes;

    public ContribuyenteService(RepositorioDeContribuyentes repositorioDeContribuyentes) {
        this.repositorioDeContribuyentes = repositorioDeContribuyentes;
    }

    @Transactional
    public void guardarContribuyente(Contribuyente contribuyente) {
        if (!repositorioDeContribuyentes.existsById(contribuyente.getContribuyenteId())) {
            repositorioDeContribuyentes.save(contribuyente);
        }
    }
}
