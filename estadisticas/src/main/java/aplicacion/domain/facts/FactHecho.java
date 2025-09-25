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

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @JoinColumn(name = "ubicacionId")
    private DimensionUbicacion dimensionUbicacion;
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("tiempoId") // vincula con FactHechoId.tiempoId
    @JoinColumn(name = "tiempoId")
    private DimensionTiempo dimensionTiempo;
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("categoriaId") // vincula con FactHechoId.categoriaId
    @JoinColumn(name = "categoriaId")
    private DimensionCategoria dimensionCategoria;

    private Long cantidadDeHechos = 1L;


    public FactHecho(DimensionUbicacion dimensionUbicacion, DimensionTiempo dimensionTiempo, DimensionCategoria dimensionCategoria, Long cantidadDeHechos) {
        this.dimensionUbicacion = dimensionUbicacion;
        this.dimensionTiempo = dimensionTiempo;
        this.dimensionCategoria = dimensionCategoria;
        this.id = new FactHechoId(dimensionUbicacion.getId_ubicacion(), dimensionTiempo.getIdTiempo(), dimensionCategoria.getIdCategoria());
        this.cantidadDeHechos = cantidadDeHechos;
    }

    public FactHecho() {

    }


    public static FactHecho fromHecho(Hecho hecho) {
        FactHecho factHecho = new FactHecho();
        Provincia prov = IdentificadorDeUbicacion.getInstance().identificar(hecho.getUbicacion().getLatitud(), hecho.getUbicacion().getLongitud());
        factHecho.setDimensionUbicacion(new DimensionUbicacion(prov.getProvincia(), prov.getPais()));
        factHecho.setDimensionTiempo(new DimensionTiempo(hecho.getFechaAcontecimiento()));
        factHecho.setDimensionCategoria(new DimensionCategoria(hecho.getCategoria().getNombre()));
        return factHecho;
    }
//ayuda-help me-ayuda(en chino)


    public String toString(){
        return "FactHecho{" +
                "id=" + id +
                ", dimensionUbicacion=" + dimensionUbicacion +
                ", dimensionTiempo=" + dimensionTiempo +
                ", dimensionCategoria=" + dimensionCategoria +
                ", cantidadDeHechos=" + cantidadDeHechos +
                '}';
    }

    public void sumarOcurrencia() {
        cantidadDeHechos++;
    }
    public void sumarOcurrencias(Long i) {
        cantidadDeHechos += i;
    }
}
