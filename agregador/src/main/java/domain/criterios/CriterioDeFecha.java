package domain.criterios;

import domain.hechos.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// CRITERIO DE FECHA
@Entity
@DiscriminatorValue("FECHA")
@NoArgsConstructor
public class CriterioDeFecha extends CriterioDePertenencia{
    private LocalDateTime fechaInicial;
    private LocalDateTime fechaFinal;

    public CriterioDeFecha(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
    }

    @Override
    public Boolean cumpleCriterio(Hecho hecho){
        return hecho.ocurrioEntre(fechaInicial, fechaFinal);
    }
}