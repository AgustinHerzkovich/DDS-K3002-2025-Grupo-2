package aplicacion.dto.mappers;

import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import aplicacion.dto.output.ContribuyenteOutputDTO;
import aplicacion.dto.output.IdentidadContribuyenteOutputDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContribuyenteOutputMapper {
    private final IdentidadContribuyenteOutputMapper identidadContribuyenteOutputMapper;

    public ContribuyenteOutputMapper() {
        this.identidadContribuyenteOutputMapper = identidadContribuyenteOutputMapper;
    }
    public ContribuyenteOutputDTO map(Contribuyente contribuyente) {
        List<IdentidadContribuyente> identidades = contribuyente.getIdentidades();
        List<IdentidadContribuyenteOutputDTO> identidadesOutput = new ArrayList<>();
        for (IdentidadContribuyente identidad : identidades) {
            identidadesOutput.add(identidadContribuyenteOutputMapper.map(identidad));
        }
        return new ContribuyenteOutputDTO(contribuyente.getId(), contribuyente.getEsAdministrador(), identidadesOutput);
    }
}