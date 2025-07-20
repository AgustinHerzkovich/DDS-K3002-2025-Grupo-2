package domain.colecciones.fuentes;

import domain.colecciones.Coleccion;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class FuenteXColeccion {
    @Setter
    @EmbeddedId
    private FuenteXColeccionId id;

    @Getter
    @ManyToOne
    @MapsId("fuenteId")
    private Fuente fuente;

    @Getter
    @ManyToOne
    @MapsId("coleccionId")
    private Coleccion coleccion;

    public FuenteXColeccion(Fuente fuente, Coleccion coleccion) {
        this.fuente = fuente;
        this.coleccion = coleccion;
        this.id = new FuenteXColeccionId(fuente.getId(), coleccion.getIdentificadorHandle());
    }
}
