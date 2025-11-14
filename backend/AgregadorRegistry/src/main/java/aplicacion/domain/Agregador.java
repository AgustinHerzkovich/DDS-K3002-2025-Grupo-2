package aplicacion.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Agregador {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String agregadorID;

    private boolean tieneApiAdministrativa;

    private boolean tieneApiPublica;

    public Agregador() {
        this.tieneApiAdministrativa = false;
        this.tieneApiPublica = false;
    }

}
