package aplicacion.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@AllArgsConstructor
@Getter
@Setter
public class IdentidadContribuyenteInputDto {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
}
