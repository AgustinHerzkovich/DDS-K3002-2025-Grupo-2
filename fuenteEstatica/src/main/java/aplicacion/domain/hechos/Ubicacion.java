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

    public double distanciaA(Ubicacion otraUbicacion) {
        return haversine(this.latitud, this.longitud, otraUbicacion.getLatitud(), otraUbicacion.getLongitud());
    }

    public Double haversine(Double latitud1, Double longitud1, Double latitud2, Double longitud2) {
        final int R = 6371; // Radio de la Tierra en km

        Double dLat = Math.toRadians(latitud2 - latitud1);
        Double dLon = Math.toRadians(longitud2 - longitud1);

        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(latitud1)) * Math.cos(Math.toRadians(latitud2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distancia en km
    }
}