package domain.usuarios;

import domain.hechos.Hecho;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;


// IDENTIDAD CONTRIBUYENTE
@Entity
public class IdentidadContribuyente {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    @ManyToOne
    private Contribuyente contribuyente;
    private List<Hecho> hechosContribuidos;

    public IdentidadContribuyente(String nombre, String apellido, LocalDate fechaNacimiento, Contribuyente contribuyente){
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.contribuyente = contribuyente;
    }

    public IdentidadContribuyente() {

    }

    public Integer getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public void agregarHechoContrubuido(Hecho hecho) { this.hechosContribuidos.add(hecho); }
}