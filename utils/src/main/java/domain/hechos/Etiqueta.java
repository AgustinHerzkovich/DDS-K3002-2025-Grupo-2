package domain.hechos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

// ETIQUETA
@Entity
public class Etiqueta {
    @Getter
    private String nombre;
    private String descripcion;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Etiqueta(String nombre, String descripcion){
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Etiqueta() {

    }

    public boolean esIdenticaA(String etiqueta_nombre) {
        return this.nombre.equals(etiqueta_nombre);
    }
}