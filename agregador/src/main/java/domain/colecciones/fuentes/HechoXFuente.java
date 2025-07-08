package domain.colecciones.fuentes;

import domain.hechos.Hecho;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Getter
public class HechoXFuente {

    @EmbeddedId
    private HechoXFuenteId id;

    @ManyToOne
    @MapsId("hechoId")
    private Hecho hecho;

    @ManyToOne
    @MapsId("fuenteId")
    private Fuente fuente;

    public HechoXFuente(Hecho hecho, Fuente fuente) {
        this.hecho = hecho;
        this.fuente = fuente;
        this.id = new HechoXFuenteId(hecho.getId(), fuente.getId());
    }
}