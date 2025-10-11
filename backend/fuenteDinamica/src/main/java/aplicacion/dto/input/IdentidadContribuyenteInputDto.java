package aplicacion.dto.input;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class IdentidadContribuyenteInputDto {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
}
