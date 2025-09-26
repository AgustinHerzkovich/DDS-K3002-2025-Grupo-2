package aplicacion.domain.dimensiones;

import aplicacion.domain.hechosYSolicitudes.Coleccion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.apache.commons.lang3.builder.HashCodeExclude;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class DimensionColeccion {
    @HashCodeExclude
    @Id
    @Column(name = "coleccionId")
    @GeneratedValue(strategy = IDENTITY)
    private Long idColeccion;

    private String idColeccionAgregador;
    @HashCodeExclude
    private String titulo;
    @HashCodeExclude
    private String descripcion;

    public static DimensionColeccion fromColeccion(Coleccion coleccion){
        return new DimensionColeccion(null, coleccion.getId(), coleccion.getTitulo(), coleccion.getDescripcion());
    }

}
