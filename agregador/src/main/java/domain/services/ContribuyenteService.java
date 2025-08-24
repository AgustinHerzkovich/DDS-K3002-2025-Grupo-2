package domain.services;

import domain.repositorios.RepositorioDeContribuyentes;
import domain.usuarios.Contribuyente;
import org.hibernate.Hibernate;
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
        if (!repositorioDeContribuyentes.existsById(contribuyente.getId())) {
            repositorioDeContribuyentes.save(contribuyente);
        }
    }
    @Transactional
    public Contribuyente obtenerContribuyentePorId(Long id){
        Contribuyente c = repositorioDeContribuyentes.getById(Long.valueOf(id));
        Hibernate.initialize(c.getId());
        Hibernate.initialize(c.getUltimaIdentidad());
        Hibernate.initialize(c.getSolicitudesEliminacion());
        return c;
    }
}
