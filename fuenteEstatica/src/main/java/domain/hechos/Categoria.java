package domain.hechos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

//CATEGORIA
public class Categoria {
    @Getter
    private String nombre;
    private LocalDate fechaCreacion;

    @JsonCreator
    public Categoria(@JsonProperty("nombre") String nombre) {
        this.nombre = nombre;
        this.fechaCreacion = LocalDate.now();
    }

    public Boolean esIdenticaA(String categoria_nombre) {
        return this.nombre.equals(categoria_nombre);
    }
}

