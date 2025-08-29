package aplicacion.domain.colecciones;

import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class HechoXColeccion {
    @Setter
    @EmbeddedId
    private HechoXColeccionId id = new HechoXColeccionId();

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
        this.id = new HechoXColeccionId(hecho.getId(), coleccion.getIdentificadorHandle());
    }
}

//esta clase representa la relación entre un hecho y una colección
// contiene un identificador compuesto que incluye el ID del hecho y el ID de la colección