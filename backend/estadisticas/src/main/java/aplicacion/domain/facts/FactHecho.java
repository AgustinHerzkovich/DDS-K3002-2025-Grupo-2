package aplicacion.domain.facts;

import aplicacion.domain.dimensiones.DimensionCategoria;
import aplicacion.domain.dimensiones.DimensionTiempo;
import aplicacion.domain.dimensiones.DimensionUbicacion;
import aplicacion.domain.hechosYSolicitudes.Hecho;
import aplicacion.domain.id.FactHechoId;
import aplicacion.utils.IdentificadorDeUbicacion;
import aplicacion.utils.Provincia;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FactHecho {

    @EmbeddedId
    private FactHechoId id;
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("ubicacionId") // vincula con FactHechoId.ubicacionId
    @JoinColumn(name = "ubicacionId", insertable = false, updatable = false)
    private DimensionUbicacion dimensionUbicacion;
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("tiempoId") // vincula con FactHechoId.tiempoId
    @JoinColumn(name = "tiempoId", insertable = false, updatable = false)
    private DimensionTiempo dimensionTiempo;
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("categoriaId") // vincula con FactHechoId.categoriaId
    @JoinColumn(name = "categoriaId", insertable = false, updatable = false)
    private DimensionCategoria dimensionCategoria;

    private Long cantidadDeHechos = 1L;


    public static FactHecho fromHecho(Hecho hecho) {
        FactHecho factHecho = new FactHecho();
        Provincia prov = IdentificadorDeUbicacion.getInstance().identificar(hecho.getUbicacion().getLatitud(), hecho.getUbicacion().getLongitud());
        factHecho.setDimensionUbicacion(new DimensionUbicacion(prov.getProvincia(), prov.getPais()));
        factHecho.setDimensionTiempo(new DimensionTiempo(hecho.getFechaAcontecimiento()));
        factHecho.setDimensionCategoria(new DimensionCategoria(hecho.getCategoria().getNombre()));
        return factHecho;
    }





    public void sumarOcurrencias(Long i) {
        cantidadDeHechos += i;
    }
}
