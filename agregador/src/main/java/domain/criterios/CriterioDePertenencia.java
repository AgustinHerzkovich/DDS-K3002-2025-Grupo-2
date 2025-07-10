package domain.criterios;

import domain.hechos.Hecho;
import jakarta.persistence.*;

// CRITERIO DE PERTENENCIA
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_criterio")
public abstract class CriterioDePertenencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public abstract Boolean cumpleCriterio(Hecho hecho);
}