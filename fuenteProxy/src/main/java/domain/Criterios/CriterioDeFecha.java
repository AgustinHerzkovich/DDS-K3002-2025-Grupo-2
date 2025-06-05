package domain.Criterios;

import domain.Hechos.Hecho;

import java.time.LocalDateTime;

// CRITERIO DE FECHA
public class CriterioDeFecha extends CriterioDePertenencia {
    private LocalDateTime fecha_inicial;
    private LocalDateTime fecha_final;

    public CriterioDeFecha(LocalDateTime fecha_inicial, LocalDateTime fecha_final) {
        this.fecha_inicial = fecha_inicial;
        this.fecha_final = fecha_final;
    }

    @Override
    public Boolean cumpleCriterio(Hecho hecho){
        return hecho.ocurrioEntre(fecha_inicial, fecha_final);
    }
}