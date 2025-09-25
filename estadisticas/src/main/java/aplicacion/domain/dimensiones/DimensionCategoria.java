package aplicacion.domain.dimensiones;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class DimensionCategoria {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "categoria_id")
    private Long idCategoria;
    private String nombre;
    public DimensionCategoria(String nombre) {
        this.nombre = nombre;
    }
}
