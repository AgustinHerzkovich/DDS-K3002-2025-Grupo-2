package aplicacion.dto.mappers;

import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.dto.input.ContribuyenteInputDto;
import org.springframework.stereotype.Component;

@Component
public class ContribuyenteMapper {
    private final IdentidadContribuyenteMapper identidadContribuyenteMapper;

    public ContribuyenteMapper(IdentidadContribuyenteMapper identidadContribuyenteMapper) {
        this.identidadContribuyenteMapper = identidadContribuyenteMapper;
    }

    public Contribuyente map(ContribuyenteInputDto contribuyenteInputDto) {
        Contribuyente contribuyente = new Contribuyente(contribuyenteInputDto.getEsAdministrador());
        contribuyente.agregarIdentidad(identidadContribuyenteMapper.map(contribuyenteInputDto.getIdentidad()));
        return contribuyente;
    }
}
