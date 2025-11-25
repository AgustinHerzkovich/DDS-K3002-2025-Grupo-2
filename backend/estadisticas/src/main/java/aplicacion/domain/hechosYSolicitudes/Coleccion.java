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
@Table(name = "coleccion")
public class Coleccion{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "descripcion")
    private String descripcion;
}