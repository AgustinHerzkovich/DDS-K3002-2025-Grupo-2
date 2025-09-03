package aplicacion.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
@Getter
@AllArgsConstructor
public class IdentidadContribuyenteOutputDTO {
    private Long id;
    private String nombre;
    private String apellido;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
}
