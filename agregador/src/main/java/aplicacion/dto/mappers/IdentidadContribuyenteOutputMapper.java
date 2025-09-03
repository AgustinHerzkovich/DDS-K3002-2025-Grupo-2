package aplicacion.dto.mappers;

import aplicacion.domain.usuarios.IdentidadContribuyente;
import aplicacion.dto.output.IdentidadContribuyenteOutputDTO;
import org.springframework.stereotype.Component;

@Component
public class IdentidadContribuyenteOutputMapper {
    public IdentidadContribuyenteOutputDTO map(IdentidadContribuyente identidadContribuyente) {
        return identidadContribuyente == null ? null : new IdentidadContribuyenteOutputDTO(
                identidadContribuyente.getId(),
                identidadContribuyente.getNombre(),
                identidadContribuyente.getApellido(),
                identidadContribuyente.getFechaNacimiento()
        );
    }
}
