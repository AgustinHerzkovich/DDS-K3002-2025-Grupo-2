package aplicacion.domain.hechos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

// UBICACION
@Getter
public class Ubicacion {
    private Double latitud;
    private Double longitud;

    @JsonCreator
    public Ubicacion(@JsonProperty("latitud") Double latitud, @JsonProperty("longitud") Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
}