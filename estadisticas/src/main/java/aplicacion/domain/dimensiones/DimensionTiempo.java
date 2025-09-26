package aplicacion.domain.dimensiones;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeExclude;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@EqualsAndHashCode

public class DimensionTiempo {
    @HashCodeExclude
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "tiempo_id")
    private Long idTiempo;
    private Integer anio;
    private Integer mes;
    private Integer dia;
    private Integer hora;
    public DimensionTiempo(LocalDateTime fechaAcontecimiento) {
        this.anio = fechaAcontecimiento.getYear();
        this.mes = fechaAcontecimiento.getMonthValue();
        this.dia = fechaAcontecimiento.getDayOfMonth();
        this.hora = fechaAcontecimiento.getHour();
    }

    public DimensionTiempo() {

    }


    public String getCodigo() {
        return String.join("|", anio.toString(), mes.toString(), dia.toString(), hora.toString());
    }
}
