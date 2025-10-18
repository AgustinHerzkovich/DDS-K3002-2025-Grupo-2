package aplicacion.services;

import aplicacion.dto.mappers.ContribuyenteInputMapper;
import aplicacion.dto.mappers.ContribuyenteOutputMapper;
import aplicacion.dto.mappers.IdentidadContribuyenteInputMapper;
import aplicacion.dto.mappers.IdentidadContribuyenteOutputMapper;
import aplicacion.dto.input.ContribuyenteInputDto;
import aplicacion.dto.output.ContribuyenteOutputDto;
import aplicacion.excepciones.MailYaExisteException;
import aplicacion.excepciones.ContribuyenteNoConfiguradoException;
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
    public ContribuyenteOutputDto guardarContribuyente(ContribuyenteInputDto contribuyente) throws MailYaExisteException {
        if (!repositorioDeContribuyentes.existsByMail(contribuyente.getMail())) {
            Contribuyente contribuyenteGuardado = repositorioDeContribuyentes.save(contribuyenteInputMapper.map(contribuyente));
            return contribuyenteOutputMapper.map(contribuyenteGuardado);
        } else {
            throw new MailYaExisteException("El mail " + contribuyente.getMail() + " ya existe en la base de datos.");
        }
    }

    @Transactional
    public Contribuyente obtenerContribuyentePorId(Long id) { // Lo usamos para cargar hechos
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

    public Contribuyente obtenerContribuyente(Long id) throws ContribuyenteNoConfiguradoException { // Lo usamos para consultar si un contribuyente existe a la hora de login
        return repositorioDeContribuyentes.findById(id)
                .orElseThrow(() -> new ContribuyenteNoConfiguradoException("Contribuyente no encontrado con ID: " + id));
    }

    public ContribuyenteOutputDto obtenerContribuyentePorMail(String mail) throws ContribuyenteNoConfiguradoException {
        return repositorioDeContribuyentes.findByMail(mail).map(contribuyenteOutputMapper::map)
                .orElseThrow(() -> new ContribuyenteNoConfiguradoException("Contribuyente no encontrado con mail: " + mail));
    }
}
