package domain.criterios;

import domain.hechos.Hecho;
import domain.hechos.Ubicacion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

// CRITERIO DE DISTANCIA
@Entity
@DiscriminatorValue("DISTANCIA")
@NoArgsConstructor
public class CriterioDeDistancia extends CriterioDePertenencia {
    private Ubicacion ubicacionbase;
    private Double distanciaMinima;

    public CriterioDeDistancia(Ubicacion ubicacionbase, Double distanciaMinima) {
        this.ubicacionbase = ubicacionbase;
        this.distanciaMinima = distanciaMinima;
    }

    @Override
    public Boolean cumpleCriterio(Hecho hecho){
        Ubicacion ubicacionHecho = hecho.getUbicacion();
        return ubicacionbase.distanciaA(ubicacionHecho) >= distanciaMinima;
    }
}
