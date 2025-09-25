package aplicacion.domain.facts;

import aplicacion.domain.dimensiones.DimensionEstado;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class FactSolicitud {
    @Id
    @Column(name = "id_solicitud", nullable = false)
    private Long idSolicitud;

    private Long cantidadDeSolicitudes;

    @ManyToOne
    private DimensionEstado dimensionEstado;


}
