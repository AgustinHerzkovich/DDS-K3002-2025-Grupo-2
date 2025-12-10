package aplicacion.domain.criterios;

import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.hechos.Ubicacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// CRITERIO DE DISTANCIA
@Entity
@NoArgsConstructor
@Getter
@Setter
public class CriterioDeDistancia extends CriterioDePertenencia {
    @Embedded
    private Ubicacion ubicacionBase;
    private Double distanciaMaxima;

    public CriterioDeDistancia(Ubicacion ubicacionBase, Double distanciaMaxima) {
        this.ubicacionBase = ubicacionBase;
        this.distanciaMaxima = distanciaMaxima;
    }

    @Override
    public Boolean cumpleCriterio(Hecho hecho){
        Ubicacion ubicacionHecho = hecho.getUbicacion();
        return ubicacionBase.distanciaA(ubicacionHecho) <= distanciaMaxima;
    }
}
