package domain.dashboardDTOs.usuarios;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class IdentidadDTO {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private ContribuyenteDTO contribuyente;
}