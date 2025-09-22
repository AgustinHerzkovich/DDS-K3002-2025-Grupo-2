package aplicacion.domain.hechosYSolicitudes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// UBICACION
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "ubicacion")
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double latitud;
    private Double longitud;
}