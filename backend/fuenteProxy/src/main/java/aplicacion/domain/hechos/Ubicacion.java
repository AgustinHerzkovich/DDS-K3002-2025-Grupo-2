package aplicacion.domain.hechos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// UBICACION
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitud;
    private Double longitud;

    @JsonCreator
    public Ubicacion(@JsonProperty("latitud") Double latitud, @JsonProperty("longitud") Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
}