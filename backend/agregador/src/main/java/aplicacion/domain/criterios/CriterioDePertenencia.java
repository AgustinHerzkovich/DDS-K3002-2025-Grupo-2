package aplicacion.domain.criterios;

import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

// CRITERIO DE PERTENENCIA
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Getter
public abstract class CriterioDePertenencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public abstract Boolean cumpleCriterio(Hecho hecho);
}