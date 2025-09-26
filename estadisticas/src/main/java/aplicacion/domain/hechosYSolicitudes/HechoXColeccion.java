package aplicacion.domain.hechosYSolicitudes;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "hecho_coleccion")
public class HechoXColeccion {
    @Setter
    @EmbeddedId
    private HechoXColeccionId id;

    @ManyToOne
    @MapsId("hechoId")
    private Hecho hecho;

    @ManyToOne
    @MapsId("coleccionId")
    private Coleccion coleccion;

    @Setter
    private boolean consensuado;

    public HechoXColeccion(Hecho hecho, Coleccion coleccion) {
        this.hecho = hecho;
        this.coleccion = coleccion;
        this.id = new HechoXColeccionId(hecho.getId(), coleccion.getId());
    }
}