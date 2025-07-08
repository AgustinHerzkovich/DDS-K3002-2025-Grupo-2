package domain.hechos;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Getter;

//CATEGORIA
@Embeddable
public class Categoria {
    @Getter
    private String nombre;
    private LocalDateTime fechaCreacion;

    @JsonCreator
    public Categoria(@JsonProperty("nombre") String nombre) {
        this.nombre = nombre;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Categoria() {

    }

    public Boolean esIdenticaA(String categoria_nombre) {
        return this.nombre.equals(categoria_nombre);
    }
}

