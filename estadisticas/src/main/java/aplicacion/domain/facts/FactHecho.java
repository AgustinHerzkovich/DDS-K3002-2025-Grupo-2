package aplicacion.domain.facts;

import aplicacion.domain.dimensiones.DimensionCategoria;
import aplicacion.domain.dimensiones.DimensionTiempo;
import aplicacion.domain.dimensiones.DimensionUbicacion;
import aplicacion.domain.id.FactHechoId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FactHecho {

    @EmbeddedId
    private FactHechoId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("ubicacionId") // vincula con FactHechoId.ubicacionId
    @JoinColumn(name = "id_ubicacion")
    private DimensionUbicacion dimensionUbicacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("tiempoId") // vincula con FactHechoId.tiempoId
    @JoinColumn(name = "id_tiempo")
    private DimensionTiempo dimensionTiempo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("categoriaId") // vincula con FactHechoId.categoriaId
    @JoinColumn(name = "id_categoria")
    private DimensionCategoria dimensionCategoria;

    private Long cantidadDeHechos;
}
