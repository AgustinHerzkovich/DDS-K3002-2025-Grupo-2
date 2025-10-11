package aplicacion.domain.usuarios;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;

// IDENTIDAD CONTRIBUYENTE
@Entity
@NoArgsConstructor
@Getter
public class IdentidadContribuyente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    public Integer getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public String getNombreCompleto() { return nombre + " " + apellido; }
}