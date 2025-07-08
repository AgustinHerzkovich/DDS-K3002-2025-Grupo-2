package domain.fuentesEstaticas;
import domain.hechos.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// FUENTE
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_fuente")
@NoArgsConstructor

public abstract class Fuente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    public Fuente(Long id) {
        this.id = id;
    }

    public abstract List<Hecho> importarHechos();
}