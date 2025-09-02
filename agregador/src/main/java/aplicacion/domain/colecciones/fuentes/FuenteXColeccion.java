package aplicacion.domain.colecciones.fuentes;

import aplicacion.domain.colecciones.Coleccion;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    private LocalDateTime ultimaPeticion;

    public FuenteXColeccion(Fuente fuente, Coleccion coleccion) {
        this.fuente = fuente;
        this.coleccion = coleccion;
        this.id = new FuenteXColeccionId(fuente.getId(), coleccion.getId());
        this.ultimaPeticion = null; // Arranca en null para que si es la primera petición, traer todos los hechos
    }
}