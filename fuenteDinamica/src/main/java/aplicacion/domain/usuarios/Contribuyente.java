package aplicacion.domain.usuarios;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @OneToMany(mappedBy = "contribuyente", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IdentidadContribuyente> identidades;

    @JsonCreator
    public Contribuyente(@JsonProperty("esAdministrador") Boolean esAdministrador) {
        this.esAdministrador = esAdministrador;
        this.identidades = new ArrayList<>();
    }

    public IdentidadContribuyente getUltimaIdentidad() {
        if (identidades == null || identidades.isEmpty()) {
            return null;
        }
        return identidades.getLast();
    }

    public void agregarIdentidad(IdentidadContribuyente identidad){
        identidades.add(identidad);
        identidad.setContribuyente(this); // Establece la relación bidireccional
    }

    public String getNombreCompleto() {
        IdentidadContribuyente identidad = this.getUltimaIdentidad();
        if (identidad != null) {
            return identidad.getNombre() + " " + identidad.getApellido();
        }
        return "Sin identidad";
    }

    public void contribuirAlHecho(Hecho hecho) {
        getUltimaIdentidad().agregarHechoContribuido(hecho);
        hecho.setAutor(getUltimaIdentidad());
    }
}