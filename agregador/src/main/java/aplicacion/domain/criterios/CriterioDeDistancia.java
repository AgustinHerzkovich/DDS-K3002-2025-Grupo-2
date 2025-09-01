package aplicacion.domain.criterios;

import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.hechos.Ubicacion;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// CRITERIO DE DISTANCIA
@Entity
@DiscriminatorValue("distancia")
@NoArgsConstructor
@Getter
@Setter
public class CriterioDeDistancia extends CriterioDePertenencia {
    @ManyToOne
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
