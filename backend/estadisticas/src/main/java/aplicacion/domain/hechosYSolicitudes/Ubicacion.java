package aplicacion.domain.hechosYSolicitudes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// UBICACION
@NoArgsConstructor
@Getter
@Setter
@Table(name = "ubicacion")
public class Ubicacion {
    private Double latitud;
    private Double longitud;

    @Override
    public String toString() {
        return "Ubicacion{" +
                "latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }
}