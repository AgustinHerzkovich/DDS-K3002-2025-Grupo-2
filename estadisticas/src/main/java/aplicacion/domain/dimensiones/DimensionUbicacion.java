package aplicacion.domain.dimensiones;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class DimensionUbicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_ubicacion;
    private String pais = "Argentina";
    private String provincia;
    public DimensionUbicacion(String provincia, String pais){
        this.provincia = provincia;
        this.pais = pais;
    }

    public DimensionUbicacion() {

    }
}
