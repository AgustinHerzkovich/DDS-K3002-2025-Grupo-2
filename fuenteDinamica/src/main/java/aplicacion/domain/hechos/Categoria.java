package aplicacion.domain.hechos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

//CATEGORIA
@Entity
@NoArgsConstructor
@Getter
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String nombre;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public Boolean esIdenticaA(String otraCategoria) {
        return this.nombre.equals(otraCategoria);
    }
}

