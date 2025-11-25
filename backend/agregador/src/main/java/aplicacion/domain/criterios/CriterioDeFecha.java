package aplicacion.domain.criterios;

import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// CRITERIO DE FECHA
@Entity
@NoArgsConstructor
@Getter
@Setter
public class CriterioDeFecha extends CriterioDePertenencia{
    @Column
    private LocalDateTime fechaInicial;
    @Column
    private LocalDateTime fechaFinal;

    public CriterioDeFecha(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
    }

    @Override
    public Boolean cumpleCriterio(Hecho hecho){
        LocalDateTime fechaAcontecimiento = hecho.getFechaAcontecimiento();
        return fechaAcontecimiento.isAfter(fechaInicial) && fechaAcontecimiento.isBefore(fechaFinal);
    }
}