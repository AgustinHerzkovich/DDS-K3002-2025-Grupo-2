package aplicacion.domain.hechos;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// UBICACION
@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class Ubicacion {
    private Double latitud;
    private Double longitud;

    public Ubicacion(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public double distanciaA(Ubicacion otraUbicacion) {
        return haversine(this.latitud, this.longitud, otraUbicacion.getLatitud(), otraUbicacion.getLongitud());
    }

    public Double haversine(Double latitud1, Double longitud1, Double latitud2, Double longitud2) {
        final int R = 6371; // Radio de la Tierra en km

        double dLat = Math.toRadians(latitud2 - latitud1);
        double dLon = Math.toRadians(longitud2 - longitud1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(latitud1)) * Math.cos(Math.toRadians(latitud2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distancia en km
    }
}