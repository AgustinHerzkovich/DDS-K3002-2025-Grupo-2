package aplicacion.domain.facts;

import aplicacion.domain.dimensiones.DimensionCategoria;
import aplicacion.domain.dimensiones.DimensionTiempo;
import aplicacion.domain.dimensiones.DimensionUbicacion;
import aplicacion.domain.hechosYSolicitudes.Hecho;
import aplicacion.domain.id.FactHechoId;
import aplicacion.utils.IdentificadorDeUbicacion;
import aplicacion.utils.Provincia;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FactHechoDTO {
    @EqualsAndHashCode.Include
    private DimensionUbicacion dimensionUbicacion;
    @EqualsAndHashCode.Include
    private DimensionTiempo dimensionTiempo;
    @EqualsAndHashCode.Include
    private DimensionCategoria dimensionCategoria;
    private Long cantidadDeHechos = 1L;


    public static FactHechoDTO fromHecho(Hecho hecho) {
        FactHechoDTO factHecho = new FactHechoDTO();
        Provincia prov = IdentificadorDeUbicacion.getInstance().identificar(hecho.getUbicacion().getLatitud(), hecho.getUbicacion().getLongitud());
        factHecho.setDimensionUbicacion(new DimensionUbicacion(prov.getProvincia(), prov.getPais()));
        factHecho.setDimensionTiempo(new DimensionTiempo(hecho.getFechaAcontecimiento()));
        factHecho.setDimensionCategoria(new DimensionCategoria(hecho.getCategoria().getNombre()));
        return factHecho;
    }
    public void sumarOcurrencia() {
        cantidadDeHechos++;
    }
    public void sumarOcurrencias(Long i) {
        cantidadDeHechos += i;
    }
}
