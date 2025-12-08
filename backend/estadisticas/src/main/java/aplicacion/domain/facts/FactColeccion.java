package aplicacion.domain.facts;

import aplicacion.domain.dimensiones.DimensionCategoria;
import aplicacion.domain.dimensiones.DimensionColeccion;
import aplicacion.domain.dimensiones.DimensionUbicacion;
import aplicacion.domain.hechosYSolicitudes.Hecho;
import aplicacion.domain.id.FactColeccionId;
import aplicacion.utils.IdentificadorDeUbicacion;
import aplicacion.utils.Provincia;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeExclude;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
public class FactColeccion {
    @EmbeddedId
    @HashCodeExclude
    private FactColeccionId id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("coleccionId")
    @JoinColumn(name = "coleccionId")
    private DimensionColeccion dimensionColeccion;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("ubicacionId")
    @JoinColumn(name = "ubicacionId")
    private DimensionUbicacion dimensionUbicacion;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("categoriaId")
    @JoinColumn(name = "categoriaId")
    private DimensionCategoria dimensionCategoria;

    private Long cantidadHechos;

    public void sumarOcurrencias(Long x){
        cantidadHechos += x;
    }
    public FactColeccion(DimensionColeccion dimensionColeccion, DimensionCategoria dimensionCategoria, DimensionUbicacion dimensionUbicacion){
        this.cantidadHechos = 1L;
        this.dimensionCategoria = dimensionCategoria;
        this.dimensionUbicacion = dimensionUbicacion;
        this.dimensionColeccion = dimensionColeccion;
    }

    public static FactColeccion fromHechoYColeccion(Hecho hecho, DimensionColeccion dimensionColeccion){
        Provincia prov = IdentificadorDeUbicacion.getInstance().identificar(hecho.getUbicacion().getLatitud(), hecho.getUbicacion().getLongitud());
        return new FactColeccion(dimensionColeccion, new DimensionCategoria(hecho.getCategoria().getNombre()), new DimensionUbicacion(prov.getProvincia(), prov.getPais()));
    }
}
