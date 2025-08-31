package aplicacion.dto.mappers;

import aplicacion.domain.usuarios.IdentidadContribuyente;
import aplicacion.dto.input.IdentidadContribuyenteInputDto;
import org.springframework.stereotype.Component;

@Component
public class IdentidadContribuyenteInputMapper {
    public IdentidadContribuyente map(IdentidadContribuyenteInputDto identidadContribuyenteInputDto) {
        return new IdentidadContribuyente(
                identidadContribuyenteInputDto.getNombre(),
                identidadContribuyenteInputDto.getApellido(),
                identidadContribuyenteInputDto.getFechaNacimiento()
        );
    }
}
