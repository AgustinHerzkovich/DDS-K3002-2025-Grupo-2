package domain.usuarios;

import com.fasterxml.jackson.annotation.JsonFormat;
import domain.hechos.Hecho;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

// IDENTIDAD CONTRIBUYENTE
@Entity
@NoArgsConstructor
public class IdentidadContribuyente {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    @ManyToOne
    private Contribuyente contribuyente;
    @OneToMany(mappedBy = "autor")
    private List<Hecho> hechosContribuidos;

    public IdentidadContribuyente(String nombre, String apellido, LocalDate fechaNacimiento, Contribuyente contribuyente){
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.contribuyente = contribuyente;
        this.hechosContribuidos = new ArrayList<>();
    }

    public Integer getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public void agregarHechoContrubuido(Hecho hecho) { this.hechosContribuidos.add(hecho); }
}