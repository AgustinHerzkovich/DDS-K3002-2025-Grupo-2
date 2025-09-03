package aplicacion.dto.mappers;

import aplicacion.domain.usuarios.IdentidadContribuyente;
import org.springframework.stereotype.Component;
import aplicacion.dto.input.IdentidadContribuyenteInputDTO;

@Component
public class IdentidadContribuyenteInputMapper {
    public IdentidadContribuyente map(IdentidadContribuyenteInputDTO identidadContribuyenteInputDto) {
        return new IdentidadContribuyente(
                identidadContribuyenteInputDto.getNombre(),
                identidadContribuyenteInputDto.getApellido(),
                identidadContribuyenteInputDto.getFechaNacimiento()
        );
    }
}
