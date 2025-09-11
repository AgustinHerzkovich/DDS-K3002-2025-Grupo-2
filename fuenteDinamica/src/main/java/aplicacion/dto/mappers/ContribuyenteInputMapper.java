package aplicacion.dto.mappers;

import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import aplicacion.dto.input.ContribuyenteInputDto;
import org.springframework.stereotype.Component;

@Component
public class ContribuyenteInputMapper {
    private final IdentidadContribuyenteInputMapper identidadContribuyenteInputMapper;

    public ContribuyenteInputMapper(IdentidadContribuyenteInputMapper identidadContribuyenteInputMapper) {
        this.identidadContribuyenteInputMapper = identidadContribuyenteInputMapper;
    }

    public Contribuyente map(ContribuyenteInputDto contribuyenteInputDto) {
        IdentidadContribuyente identidad = identidadContribuyenteInputMapper.map(contribuyenteInputDto.getIdentidad());

        return new Contribuyente(contribuyenteInputDto.getEsAdministrador(), identidad);
    }
}
