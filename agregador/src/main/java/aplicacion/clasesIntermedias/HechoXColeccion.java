package aplicacion.clasesIntermedias;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.domain.hechos.Hecho;
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

//esta clase representa la relación entre un hecho y una colección
// contiene un identificador compuesto que incluye el ID del hecho y el ID de la colección