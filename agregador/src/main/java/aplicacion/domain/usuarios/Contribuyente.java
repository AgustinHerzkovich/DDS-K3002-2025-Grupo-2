package aplicacion.domain.usuarios;

import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.solicitudes.SolicitudEliminacion;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean esAdministrador;
    @OneToMany(mappedBy = "contribuyente", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IdentidadContribuyente> identidades;
    @OneToMany(mappedBy = "solicitante", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<SolicitudEliminacion> solicitudesEliminacion;

    @JsonCreator
    public Contribuyente(@JsonProperty("esAdministrador") Boolean esAdministrador) {
        this.esAdministrador = esAdministrador;
        this.identidades = new ArrayList<>();
        this.solicitudesEliminacion = new ArrayList<>();
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

    public void agregarSolicitudEliminacion(SolicitudEliminacion solicitud) {
        solicitudesEliminacion.add(solicitud);
    }

    public String getNombreCompleto() {
        IdentidadContribuyente identidad = this.getUltimaIdentidad();
        if (identidad != null) {
            return identidad.getNombre() + " " + identidad.getApellido();
        }
        return "Sin identidad";
    }
}