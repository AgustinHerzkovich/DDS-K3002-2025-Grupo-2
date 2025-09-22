package aplicacion.domain.hechosYSolicitudes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//CATEGORIA
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String nombre;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}

