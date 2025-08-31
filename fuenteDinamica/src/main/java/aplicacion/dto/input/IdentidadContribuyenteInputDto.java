package aplicacion.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class IdentidadContribuyenteDto {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
}
