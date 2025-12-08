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
@Setter
public class Contribuyente {
    @Id
    private String id; // Usamos el id que genera Keycloak
    private Boolean esAdministrador;
    @Embedded
    private IdentidadContribuyente identidad;
    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER)
    private List<Hecho> hechosContribuidos;
    private String mail;

    public Contribuyente(String id, Boolean esAdministrador, IdentidadContribuyente identidad, String mail) {
        this.id = id;
        this.esAdministrador = esAdministrador;
        this.identidad = identidad;
        this.mail = mail;
        this.hechosContribuidos = new ArrayList<>();
    }
}