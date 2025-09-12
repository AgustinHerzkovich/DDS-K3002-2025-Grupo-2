package aplicacion.dto.mappers;

import aplicacion.domain.usuarios.IdentidadContribuyente;
import aplicacion.dto.output.IdentidadContribuyenteOutputDto;
import org.springframework.stereotype.Component;

@Component
public class IdentidadContribuyenteOutputMapper {
    public IdentidadContribuyenteOutputDto map(IdentidadContribuyente identidadContribuyente) {
        return identidadContribuyente == null ? null : new IdentidadContribuyenteOutputDto(
                identidadContribuyente.getId(),
                identidadContribuyente.getNombre(),
                identidadContribuyente.getApellido(),
                identidadContribuyente.getFechaNacimiento()
        );
    }
}
