package aplicacion.clasesIntermedias;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class FuenteXColeccion {
    @EmbeddedId
    private FuenteXColeccionId id;

    @ManyToOne
    @MapsId("fuenteId")
    private Fuente fuente;

    @ManyToOne
    @MapsId("coleccionId")
    private Coleccion coleccion;

    public FuenteXColeccion(Fuente fuente, Coleccion coleccion) {
        this.fuente = fuente;
        this.coleccion = coleccion;
        this.id = new FuenteXColeccionId(fuente.getId(), coleccion.getId());
    }
}