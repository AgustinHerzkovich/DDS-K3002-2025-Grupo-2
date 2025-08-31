package aplicacion.dto.mappers;

import aplicacion.domain.usuarios.IdentidadContribuyente;
import aplicacion.dto.IdentidadContribuyenteDto;
import org.springframework.stereotype.Component;

@Component
public class IdentidadContribuyenteMapper {
    public IdentidadContribuyente map(IdentidadContribuyenteDto identidadContribuyenteDto) {
        return new IdentidadContribuyente(
                identidadContribuyenteDto.getNombre(),
                identidadContribuyenteDto.getApellido(),
                identidadContribuyenteDto.getFechaNacimiento()
        );
    }
}
