package domain.criterios;

import domain.hechos.Hecho;

import java.time.LocalDate;

// CRITERIO DE FECHA
public class CriterioDeFecha implements CriterioDePertenencia{
    private final LocalDate fechaInicial;
    private final LocalDate fechaFinal;

    public CriterioDeFecha(LocalDate fechaInicial, LocalDate fechaFinal) {
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
    }

    @Override
    public Boolean cumpleCriterio(Hecho hecho){
        return hecho.ocurrioEntre(fechaInicial, fechaFinal);
    }
}