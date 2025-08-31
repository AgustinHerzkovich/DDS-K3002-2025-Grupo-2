package aplicacion.dto.mappers;

import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.dto.input.ContribuyenteInputDto;
import org.springframework.stereotype.Component;

@Component
public class ContribuyenteInputMapper {
    private final IdentidadContribuyenteInputMapper identidadContribuyenteInputMapper;

    public ContribuyenteInputMapper(IdentidadContribuyenteInputMapper identidadContribuyenteInputMapper) {
        this.identidadContribuyenteInputMapper = identidadContribuyenteInputMapper;
    }

    public Contribuyente map(ContribuyenteInputDto contribuyenteInputDto) {
        Contribuyente contribuyente = new Contribuyente(contribuyenteInputDto.getEsAdministrador());
        if (contribuyenteInputDto.getIdentidad() != null) {
            contribuyente.agregarIdentidad(identidadContribuyenteInputMapper.map(contribuyenteInputDto.getIdentidad()));
        }
        return contribuyente;
    }
}
