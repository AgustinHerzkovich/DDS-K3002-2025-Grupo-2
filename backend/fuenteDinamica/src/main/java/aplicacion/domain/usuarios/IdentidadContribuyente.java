package aplicacion.domain.usuarios;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

// IDENTIDAD CONTRIBUYENTE
@Embeddable
@NoArgsConstructor
@Getter
public class IdentidadContribuyente {
    @Column(length = 20)
    private String nombre;
    @Column(length = 20)
    private String apellido;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaNacimiento;

    public IdentidadContribuyente(String nombre, String apellido, LocalDate fechaNacimiento){
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }
}