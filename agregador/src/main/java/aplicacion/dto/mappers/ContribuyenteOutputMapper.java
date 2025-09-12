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
        this.identidadContribuyenteOutputMapper = new IdentidadContribuyenteOutputMapper();
    }
    public ContribuyenteOutputDTO map(Contribuyente contribuyente) {
        IdentidadContribuyente identidad = contribuyente.getIdentidad();
        IdentidadContribuyenteOutputDTO identidadOutput = identidadContribuyenteOutputMapper.map(identidad);
        return new ContribuyenteOutputDTO(contribuyente.getId(), contribuyente.getEsAdministrador(), identidadOutput);
    }
}