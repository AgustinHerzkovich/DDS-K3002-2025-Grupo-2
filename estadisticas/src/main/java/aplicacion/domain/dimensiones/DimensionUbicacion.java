package aplicacion.domain.dimensiones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeExclude;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class DimensionUbicacion {
    @HashCodeExclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ubicacion_id")
    private Long id_ubicacion;
    private String pais = "Argentina";
    private String provincia;
    public DimensionUbicacion(String provincia, String pais){
        this.provincia = provincia;
        this.pais = pais;
    }

    public DimensionUbicacion() {

    }

    public String getCodigo() {
        return String.join("|", this.pais, this.provincia);
    }
}
