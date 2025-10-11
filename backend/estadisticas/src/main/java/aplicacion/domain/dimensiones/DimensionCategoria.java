package aplicacion.domain.dimensiones;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.apache.commons.lang3.builder.HashCodeExclude;

import static jakarta.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode
public class DimensionCategoria {
    @HashCodeExclude
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "categoria_id")
    private Long idCategoria;
    private String nombre;
    public DimensionCategoria(String nombre) {
        this.nombre = nombre;
    }
}
