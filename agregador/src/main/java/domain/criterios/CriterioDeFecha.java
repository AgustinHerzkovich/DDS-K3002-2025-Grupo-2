package domain.criterios;

import domain.hechos.Hecho;

import java.time.LocalDateTime;

// CRITERIO DE FECHA
public class CriterioDeFecha implements CriterioDePertenencia{
    private final LocalDateTime fechaInicial;
    private final LocalDateTime fechaFinal;

    public CriterioDeFecha(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
    }

    @Override
    public Boolean cumpleCriterio(Hecho hecho){
        return hecho.ocurrioEntre(fechaInicial, fechaFinal);
    }
}