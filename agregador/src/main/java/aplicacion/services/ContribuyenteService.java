package aplicacion.services;

import aplicacion.dto.input.ContribuyenteInputDTO;
import aplicacion.dto.input.IdentidadContribuyenteInputDTO;
import aplicacion.dto.mappers.ContribuyenteInputMapper;
import aplicacion.dto.mappers.ContribuyenteOutputMapper;
import aplicacion.dto.mappers.IdentidadContribuyenteInputMapper;
import aplicacion.dto.mappers.IdentidadContribuyenteOutputMapper;
import aplicacion.dto.output.ContribuyenteOutputDTO;
import aplicacion.dto.output.IdentidadContribuyenteOutputDTO;
import aplicacion.repositorios.RepositorioDeContribuyentes;
import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContribuyenteService {
    private final RepositorioDeContribuyentes repositorioDeContribuyentes;
    private final IdentidadContribuyenteInputMapper identidadContribuyenteInputMapper;
    private final ContribuyenteInputMapper contribuyenteInputMapper;
    private final ContribuyenteOutputMapper contribuyenteOutputMapper;
    private final IdentidadContribuyenteOutputMapper identidadContribuyenteOutputMapper;


    public ContribuyenteService(RepositorioDeContribuyentes repositorioDeContribuyentes, ContribuyenteOutputMapper contribuyenteOutputMapper,ContribuyenteInputMapper contribuyenteInputMapper,IdentidadContribuyenteOutputMapper identidadContribuyenteOutputMapper,IdentidadContribuyenteInputMapper identidadContribuyenteInputMapper) {
        this.repositorioDeContribuyentes = repositorioDeContribuyentes;
        this.identidadContribuyenteInputMapper = identidadContribuyenteInputMapper;
        this.identidadContribuyenteOutputMapper = identidadContribuyenteOutputMapper;
        this.contribuyenteOutputMapper = contribuyenteOutputMapper;
        this.contribuyenteInputMapper = contribuyenteInputMapper;
    }/*

    @Transactional
    public ContribuyenteOutputDTO guardarContribuyente(ContribuyenteInputDTO contribuyente) {
        if (!repositorioDeContribuyentes.existsById(contribuyente.getId())) {
            repositorioDeContribuyentes.save(contribuyenteInputMapper.map(contribuyente));
        }
    }*/
    @Transactional
    public Contribuyente obtenerContribuyentePorId(Long id) {
        return repositorioDeContribuyentes.findById(id)
                .map(contribuyente -> {
                    Hibernate.initialize(contribuyente.getId());
                    Hibernate.initialize(contribuyente.getUltimaIdentidad());
                    Hibernate.initialize(contribuyente.getSolicitudesEliminacion());
                    return contribuyente;
                })
                .orElseGet(() -> { // TODO: Cambiar esto
                    // Si no existe, lo creamos
                    Contribuyente nuevo = new Contribuyente(false);
                    nuevo.setId(id);
                    return repositorioDeContribuyentes.save(nuevo);
                });
    }
    @Transactional
    public ContribuyenteOutputDTO agregarIdentidadAContribuyente(Long id, IdentidadContribuyenteInputDTO identidad) {
        Contribuyente contribuyente = obtenerContribuyentePorId(id);
        contribuyente.agregarIdentidad(identidadContribuyenteInputMapper.map(identidad));
        repositorioDeContribuyentes.save(contribuyente);
        return contribuyenteOutputMapper.map(contribuyente);
    }
}
