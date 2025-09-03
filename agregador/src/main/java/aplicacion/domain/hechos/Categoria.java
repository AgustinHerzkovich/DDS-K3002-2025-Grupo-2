package aplicacion.domain.hechos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

//CATEGORIA
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String nombre;

    @JsonCreator
    public Categoria(@JsonProperty("nombre") String nombre) {
        this.nombre = nombre;
    }

    public Boolean esIdenticaA(String categoria_nombre) {
        return this.nombre.equals(categoria_nombre);
    }
}

