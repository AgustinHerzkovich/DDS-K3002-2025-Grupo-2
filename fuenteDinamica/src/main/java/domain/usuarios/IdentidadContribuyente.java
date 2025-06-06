package domain.usuarios;

import java.time.LocalDate;
import java.time.Period;


// IDENTIDAD CONTRIBUYENTE
public class IdentidadContribuyente{
    private String nombre;
    private String apellido;
    private LocalDate fecha_nacimiento;
    private Contribuyente contribuyente;

    public IdentidadContribuyente(String nombre, String apellido, LocalDate fecha_nacimiento,  Contribuyente contribuyente){
        this.nombre = nombre;
        this.apellido = apellido;
        this.fecha_nacimiento = fecha_nacimiento;
        this.contribuyente = contribuyente;
    }

    public Integer getEdad() {
        return Period.between(fecha_nacimiento, LocalDate.now()).getYears();
    }
}