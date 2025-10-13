package aplicacion.services;

import aplicacion.dto.mappers.ContribuyenteInputMapper;
import aplicacion.dto.mappers.ContribuyenteOutputMapper;
import aplicacion.dto.mappers.IdentidadContribuyenteInputMapper;
import aplicacion.dto.mappers.IdentidadContribuyenteOutputMapper;
import aplicacion.dto.input.ContribuyenteInputDto;
import aplicacion.dto.output.ContribuyenteOutputDto;
import aplicacion.excepciones.MailYaExisteException;
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
    }

    @Transactional
    public ContribuyenteOutputDto guardarContribuyente(ContribuyenteInputDto contribuyente) {
        if (!repositorioDeContribuyentes.existsByMail(contribuyente.getMail())) {
            Contribuyente contribuyenteGuardado = repositorioDeContribuyentes.save(contribuyenteInputMapper.map(contribuyente));
            return contribuyenteOutputMapper.map(contribuyenteGuardado);
        } else {
            throw new MailYaExisteException("El mail " + contribuyente.getMail() + " ya existe en la base de datos.");
        }
    }

    @Transactional
    public Contribuyente obtenerContribuyentePorId(Long id) {
        return repositorioDeContribuyentes.findById(id)
                .map(contribuyente -> {
                    Hibernate.initialize(contribuyente.getId());
                    Hibernate.initialize(contribuyente.getIdentidad());
                    Hibernate.initialize(contribuyente.getSolicitudesEliminacion());
                    return contribuyente;
                })
                .orElseGet(() -> { // TODO: Cambiar esto
                    // Si no existe, lo creamos
                    Contribuyente nuevo = new Contribuyente(false, new IdentidadContribuyente("NombrePorDefecto", "ApellidoPorDefecto", null), "mailPorDefecto");
                    nuevo.setId(id);
                    return repositorioDeContribuyentes.save(nuevo);
                });
    }
}
