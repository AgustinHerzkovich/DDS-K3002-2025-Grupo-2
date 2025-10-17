package aplicacion.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class RegistroUsuarioDto {

    // --- Campos de Autenticación (Para Keycloak) ---
    private String email;
    private String password;

    // --- Campos de Identidad (Para IdentidadContribuyente) ---
    private String nombre;
    private String apellido;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;

    // NOTA: No es necesario incluir 'esAdministrador' en el DTO de registro,
    // ya que ese valor se establecerá a 'false' por defecto en el servicio.

    // Constructor, getters y setters son manejados por Lombok.
}