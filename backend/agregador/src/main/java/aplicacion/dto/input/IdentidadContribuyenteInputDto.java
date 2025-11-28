package aplicacion.dto.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentidadContribuyenteInputDto {
    @Size(max = 20, message = "El nombre no puede tener más de 20 caracteres")
    private String nombre;
    @Size(max = 20, message = "El apellido no puede tener más de 20 caracteres")
    private String apellido;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
}
