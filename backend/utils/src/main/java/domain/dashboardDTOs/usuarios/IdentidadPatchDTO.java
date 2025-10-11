package domain.dashboardDTOs.usuarios;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentidadPatchDTO {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
}
