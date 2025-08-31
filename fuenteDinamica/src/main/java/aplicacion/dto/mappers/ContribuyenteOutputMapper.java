package aplicacion.dto.mappers;

import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import aplicacion.dto.output.ContribuyenteOutputDto;
import aplicacion.dto.output.IdentidadContribuyenteOutputDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContribuyenteOutputMapper {
    private final IdentidadContribuyenteOutputMapper identidadContribuyenteOutputMapper;

    public ContribuyenteOutputMapper(IdentidadContribuyenteOutputMapper identidadContribuyenteOutputMapper) {
        this.identidadContribuyenteOutputMapper = identidadContribuyenteOutputMapper;
    }
    public ContribuyenteOutputDto map(Contribuyente contribuyente) {
        List<IdentidadContribuyente> identidades = contribuyente.getIdentidades();
        List<IdentidadContribuyenteOutputDto> identidadesOutput = new ArrayList<>();
        for (IdentidadContribuyente identidad : identidades) {
            identidadesOutput.add(identidadContribuyenteOutputMapper.map(identidad));
        }
        return new ContribuyenteOutputDto(contribuyente.getId(), contribuyente.getEsAdministrador(), identidadesOutput);
    }
}