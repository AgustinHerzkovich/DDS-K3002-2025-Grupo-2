package aplicacion.services;

import aplicacion.dto.input.IdentidadContribuyenteInputDto;
import aplicacion.dto.mappers.ContribuyenteInputMapper;
import aplicacion.dto.mappers.ContribuyenteOutputMapper;
import aplicacion.dto.mappers.IdentidadContribuyenteInputMapper;
import aplicacion.dto.input.ContribuyenteInputDto;
import aplicacion.dto.output.ContribuyenteOutputDto;
import aplicacion.excepciones.MailYaExisteException;
import aplicacion.excepciones.ContribuyenteNoConfiguradoException;
import aplicacion.repositories.ContribuyenteRepository;
import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContribuyenteService {
    private final ContribuyenteRepository contribuyenteRepository;
    private final IdentidadContribuyenteInputMapper identidadContribuyenteInputMapper;
    private final ContribuyenteInputMapper contribuyenteInputMapper;
    private final ContribuyenteOutputMapper contribuyenteOutputMapper;

    public ContribuyenteService(ContribuyenteRepository contribuyenteRepository, ContribuyenteOutputMapper contribuyenteOutputMapper, ContribuyenteInputMapper contribuyenteInputMapper, IdentidadContribuyenteInputMapper identidadContribuyenteInputMapper) {
        this.contribuyenteRepository = contribuyenteRepository;
        this.identidadContribuyenteInputMapper = identidadContribuyenteInputMapper;
        this.contribuyenteOutputMapper = contribuyenteOutputMapper;
        this.contribuyenteInputMapper = contribuyenteInputMapper;
    }

    @Transactional
    public ContribuyenteOutputDto guardarContribuyente(ContribuyenteInputDto contribuyente) throws MailYaExisteException {
        if (!contribuyenteRepository.existsByMail(contribuyente.getMail())) {
            Contribuyente contribuyenteGuardado = contribuyenteRepository.save(contribuyenteInputMapper.map(contribuyente));
            return contribuyenteOutputMapper.map(contribuyenteGuardado);
        } else {
            throw new MailYaExisteException("El mail " + contribuyente.getMail() + " ya existe en la base de datos.");
        }
    }

    @Transactional
    public Contribuyente guardarOActualizar(Contribuyente contribuyente) {
        Optional<Contribuyente> contribuyenteExistente = contribuyenteRepository.findByMail(contribuyente.getMail());

        if (contribuyenteExistente.isPresent()) {
            Contribuyente contribuyenteGuardado = contribuyenteExistente.get();
            contribuyenteGuardado.setIdentidad(contribuyente.getIdentidad()); // Si el contribuyente ya existe por mail, le updateamos la identidad
            return contribuyenteRepository.save(contribuyenteGuardado);
        } else {
            return contribuyenteRepository.save(contribuyente); // Si no existe, lo creamos
        }
    }

    @Transactional
    public Contribuyente obtenerContribuyentePorId(String id) throws ContribuyenteNoConfiguradoException { // Lo usamos para crear/actualizar solicitudes de eliminación
        return contribuyenteRepository.findById(id)
                .map(contribuyente -> {
                    Hibernate.initialize(contribuyente.getId());
                    Hibernate.initialize(contribuyente.getIdentidad());
                    Hibernate.initialize(contribuyente.getSolicitudesEliminacion());
                    return contribuyente;
                }).orElseThrow(() -> new ContribuyenteNoConfiguradoException("No existe el contribuyente con ID: " + id));
    }

    public Contribuyente obtenerContribuyente(String id) throws ContribuyenteNoConfiguradoException { // Lo usamos para consultar si un contribuyente existe a la hora de login
        return contribuyenteRepository.findById(id)
                .orElseThrow(() -> new ContribuyenteNoConfiguradoException("Contribuyente no encontrado con ID: " + id));
    }

    public ContribuyenteOutputDto obtenerContribuyentePorMail(String mail) throws ContribuyenteNoConfiguradoException {
        return contribuyenteRepository.findByMail(mail).map(contribuyenteOutputMapper::map)
                .orElseThrow(() -> new ContribuyenteNoConfiguradoException("Contribuyente no encontrado con mail: " + mail));
    }

    public List<ContribuyenteOutputDto> obtenerContribuyentes() {
        List<Contribuyente> contribuyentes = contribuyenteRepository.findAll();
        return contribuyentes.stream().map(contribuyenteOutputMapper::map).toList();
    }

    public ContribuyenteOutputDto modificarIdentidadAContribuyente(String id, IdentidadContribuyenteInputDto identidadContribuyenteInputDto) throws ContribuyenteNoConfiguradoException {
        IdentidadContribuyente identidad = identidadContribuyenteInputMapper.map(identidadContribuyenteInputDto);
        Contribuyente contribuyente = obtenerContribuyente(id);
        contribuyente.setIdentidad(identidad);
        contribuyente = contribuyenteRepository.save(contribuyente);
        return contribuyenteOutputMapper.map(contribuyente);
    }
}
