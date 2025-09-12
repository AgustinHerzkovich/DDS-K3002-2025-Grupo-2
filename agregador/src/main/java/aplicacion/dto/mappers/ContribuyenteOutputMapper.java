package aplicacion.dto.mappers;

import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import aplicacion.dto.output.ContribuyenteOutputDto;
import aplicacion.dto.output.IdentidadContribuyenteOutputDto;
import org.springframework.stereotype.Component;

@Component
public class ContribuyenteOutputMapper {
    private final IdentidadContribuyenteOutputMapper identidadContribuyenteOutputMapper;

    public ContribuyenteOutputMapper() {
        this.identidadContribuyenteOutputMapper = new IdentidadContribuyenteOutputMapper();
    }
    public ContribuyenteOutputDto map(Contribuyente contribuyente) {
        IdentidadContribuyente identidad = contribuyente.getIdentidad();
        IdentidadContribuyenteOutputDto identidadOutput = identidadContribuyenteOutputMapper.map(identidad);
        return new ContribuyenteOutputDto(contribuyente.getId(), contribuyente.getEsAdministrador(), identidadOutput);
    }
}