package domain.criterios;

import domain.hechos.Hecho;

import java.time.LocalDate;

// CRITERIO DE FECHA
public class CriterioDeFecha implements CriterioDePertenencia{
    private LocalDate fecha_inicial;
    private LocalDate fecha_final;

    public CriterioDeFecha(LocalDate fecha_inicial, LocalDate fecha_final) {
        this.fecha_inicial = fecha_inicial;
        this.fecha_final = fecha_final;
    }

    @Override
    public Boolean cumpleCriterio(Hecho hecho){
        return hecho.ocurrioEntre(fecha_inicial, fecha_final);
    }
}