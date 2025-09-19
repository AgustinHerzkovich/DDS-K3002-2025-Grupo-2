package aplicacion.domain.hechosYSolicitudes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// COLECCION
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Coleccion{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

}