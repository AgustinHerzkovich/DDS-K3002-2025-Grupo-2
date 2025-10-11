package aplicacion.domain.usuarios;

import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//CONTRIBUYENTE
@Entity
@NoArgsConstructor
@Getter
public class Contribuyente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private Boolean esAdministrador;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Setter
    private IdentidadContribuyente identidad;
    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER)
    private List<Hecho> hechosContribuidos;

    public Contribuyente(Boolean esAdministrador, IdentidadContribuyente identidad) {
        this.esAdministrador = esAdministrador;
        this.identidad = identidad;
        this.hechosContribuidos = new ArrayList<>();
    }

    public void agregarHechoContribuido(Hecho hecho) { this.hechosContribuidos.add(hecho); }
}