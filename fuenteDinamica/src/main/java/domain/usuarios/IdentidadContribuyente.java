package domain.usuarios;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import domain.hechos.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

// IDENTIDAD CONTRIBUYENTE
@Entity
@NoArgsConstructor
@Getter
public class IdentidadContribuyente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    @ManyToOne(cascade = CascadeType.ALL)
    @Setter
    private Contribuyente contribuyente;
    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER)
    private List<Hecho> hechosContribuidos;

    @JsonCreator
    public IdentidadContribuyente(@JsonProperty("nombre") String nombre, @JsonProperty("apellido") String apellido,@JsonProperty("fechaNacimiento") LocalDate fechaNacimiento){
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.hechosContribuidos = new ArrayList<>();
    }
    @JsonIgnore
    public Integer getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public void agregarHechoContribuido(Hecho hecho) { this.hechosContribuidos.add(hecho); }
}