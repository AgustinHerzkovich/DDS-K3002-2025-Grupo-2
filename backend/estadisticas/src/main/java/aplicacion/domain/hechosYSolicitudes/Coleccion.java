package aplicacion.domain.hechosYSolicitudes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeExclude;

import java.util.ArrayList;
import java.util.List;


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