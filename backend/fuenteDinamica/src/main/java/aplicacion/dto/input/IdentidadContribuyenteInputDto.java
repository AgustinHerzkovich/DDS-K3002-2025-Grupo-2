package aplicacion.dto.input;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IdentidadContribuyenteInputDto {
    @Size(max = 20, message = "El nombre no puede tener más de 20 caracteres")
    private String nombre;
    @Size(max = 20, message = "El apellido no puede tener más de 20 caracteres")
    private String apellido;
    private LocalDate fechaNacimiento;
}
