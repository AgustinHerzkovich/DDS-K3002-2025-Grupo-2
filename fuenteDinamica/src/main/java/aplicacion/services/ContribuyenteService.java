package aplicacion.services;

import aplicacion.dto.input.ContribuyenteInputDto;
import aplicacion.dto.input.IdentidadContribuyenteInputDto;
import aplicacion.dto.mappers.ContribuyenteInputMapper;
import aplicacion.dto.mappers.ContribuyenteOutputMapper;
import aplicacion.dto.mappers.IdentidadContribuyenteInputMapper;
import aplicacion.dto.output.ContribuyenteOutputDto;
import aplicacion.excepciones.ContribuyenteNoConfiguradoException;
import aplicacion.repositorios.RepositorioDeContribuyentes;
import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import org.springframework.stereotype.Service;

@Service
public class ContribuyenteService {
    private final RepositorioDeContribuyentes repositorioDeContribuyentes;
    private final ContribuyenteInputMapper contribuyenteInputMapper;
    private final ContribuyenteOutputMapper contribuyenteOutputMapper;
    private final IdentidadContribuyenteInputMapper identidadContribuyenteInputMapper;

    public ContribuyenteService(RepositorioDeContribuyentes repositorioDeContribuyentes, ContribuyenteInputMapper contribuyenteInputMapper, ContribuyenteOutputMapper contribuyenteOutputMapper, IdentidadContribuyenteInputMapper identidadContribuyenteInputMapper) {
        this.repositorioDeContribuyentes = repositorioDeContribuyentes;
        this.contribuyenteInputMapper = contribuyenteInputMapper;
        this.contribuyenteOutputMapper = contribuyenteOutputMapper;
        this.identidadContribuyenteInputMapper = identidadContribuyenteInputMapper;
    }

    public ContribuyenteOutputDto guardarContribuyente(ContribuyenteInputDto contribuyenteInputDto) {
       Contribuyente contribuyente = repositorioDeContribuyentes.save(contribuyenteInputMapper.map(contribuyenteInputDto));
         return contribuyenteOutputMapper.map(contribuyente);
    }

    public Contribuyente obtenerContribuyente(Long id) throws ContribuyenteNoConfiguradoException {
        return repositorioDeContribuyentes.findById(id)
                .orElseThrow(() -> new ContribuyenteNoConfiguradoException("Contribuyente no encontrado con ID: " + id));
    }

    public IdentidadContribuyente obtenerIdentidad(Long id) throws ContribuyenteNoConfiguradoException {
        return repositorioDeContribuyentes.findIdentidadById(id)
                .orElseThrow(() -> new ContribuyenteNoConfiguradoException("Identidad no encontrada para el contribuyente con ID: " + id));
    }

    public ContribuyenteOutputDto agregarIdentidadAContribuyente(Long id, IdentidadContribuyenteInputDto identidadContribuyenteInputDto) throws ContribuyenteNoConfiguradoException {
        IdentidadContribuyente identidad = identidadContribuyenteInputMapper.map(identidadContribuyenteInputDto);
        Contribuyente contribuyente = obtenerContribuyente(id);
        contribuyente.agregarIdentidad(identidad);
        contribuyente = repositorioDeContribuyentes.save(contribuyente);
        return contribuyenteOutputMapper.map(contribuyente);
    }
}