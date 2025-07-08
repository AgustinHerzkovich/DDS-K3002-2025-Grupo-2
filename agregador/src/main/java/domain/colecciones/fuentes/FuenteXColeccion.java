package domain.colecciones.fuentes;

import domain.colecciones.Coleccion;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Setter;

public class FuenteXColeccion {
    @Setter
    @EmbeddedId
    private FuenteXColeccionId id = new FuenteXColeccionId();

    @ManyToOne
    @MapsId("fuenteId")
    private Fuente fuente;

    @ManyToOne
    @MapsId("coleccionId")
    private Coleccion coleccion;

    @Setter
    private boolean consensuado;

    public void setFuente(Fuente fuentosa) {
        this.fuente = fuentosa;
        this.id.setFuenteId(Fuente.getId());
    }

    public void setColeccion(Coleccion coleccion) {
        this.coleccion = coleccion;
        this.id.setColeccionId(coleccion.getIdentificadorHandle());
    }
}
