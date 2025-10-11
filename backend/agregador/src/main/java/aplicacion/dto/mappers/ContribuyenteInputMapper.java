package aplicacion.dto.mappers;

import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import aplicacion.dto.input.ContribuyenteInputDto;
import org.springframework.stereotype.Component;

@Component
public class ContribuyenteInputMapper implements Mapper<ContribuyenteInputDto, Contribuyente>{
    private final IdentidadContribuyenteInputMapper identidadContribuyenteInputMapper;

    public ContribuyenteInputMapper() {
        this.identidadContribuyenteInputMapper = new IdentidadContribuyenteInputMapper();
    }

    public Contribuyente map(ContribuyenteInputDto contribuyenteInputDto) {
        IdentidadContribuyente identidad = identidadContribuyenteInputMapper.map(contribuyenteInputDto.getIdentidad());
        return new Contribuyente(contribuyenteInputDto.getEsAdministrador(), identidad);
    }
}

